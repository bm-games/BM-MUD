package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.errorMsg
import net.bmgames.game.action.*
import net.bmgames.game.action.EntityAction.Type.Create
import net.bmgames.game.action.EntityAction.Type.Remove
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.findDifferentPlayerInRoom
import net.bmgames.game.commands.getRoom
import net.bmgames.message
import net.bmgames.state.model.*
import net.bmgames.state.model.Player.Normal
import net.bmgames.success
import java.time.Duration

/**
 * A playercommand which hits a player or a npc.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the player who gets hit.
 *
 * @constructor creates a complete hit command.
 */
class HitCommand : PlayerCommand("hit", message("game.hit-target-epilog")) {

    val target: String by argument(help = message("game.hit.target"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It hits the target player or npc, based on the executing player's damage.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Normal, game: Game): Either<String, List<Action>> =
        if (player.room == game.startRoom) errorMsg(message("game.hit.start-room"))
        else if (!player.canHit()) errorMsg(message("game.cooldown"))
        else either.eager {
            val room = player.getRoom(game).bind()

            val actions = mutableListOf<Action>()

            room.npcs[target]?.let { player.hitNPC(it, room, player.damage.toInt()) }?.let(actions::addAll)
            player.findDifferentPlayerInRoom(game, target)
                ?.let {
                    player.hitPlayer(
                        target = it,
                        game = game,
                        room = room,
                        damage = player.damage.toInt(),
                        hitter = player.ingameName
                    )
                }
                ?.let(actions::addAll)

            if (actions.isEmpty()) {
                errorMsg(message("game.entity-not-found", target))
            } else {
                success(actions + LastHitAction(player.left(), System.currentTimeMillis()))
            }.bind()
        }

}

internal fun Player?.hitPlayer(target: Normal, game: Game, room: Room, damage: Int, hitter: String): List<Action> {
    val newHealth = target.healthPoints - damage
    return if (newHealth <= 0) {
        listOfNotNull(
            this?.sendText(
                message("game.slay-someone", target.ingameName, target.ingameName)
                    .trimMargin()
            ),
            target.sendText(
                message("game.been-slain", hitter)
                    .trimMargin()
            ),
            MoveAction(target, game.getStartRoom()),
            InventoryAction(target, Inventory(null, emptyMap(), game.startItems)),
            HealthAction(
                target.left(),
                target.avatar.maxHealth - target.healthPoints
            )
        ) + target.inventory.allItems().map { EntityAction(Create, room, it.right()) }
    } else {
        listOfNotNull(
            this?.sendText(message("game.hp-left", target.ingameName, newHealth)),
            target.sendText(message("game.hit-by", hitter, newHealth)),
            HealthAction(target.left(), -damage)
        )
    } + if (room.npcs[hitter] is NPC.Hostile) {
        val hostile = room.npcs[hitter] as NPC.Hostile
        listOf(LastHitAction(Pair(room, hostile).right(), System.currentTimeMillis()))
    } else emptyList()
}

internal fun Player.hitNPC(npc: NPC, room: Room, damage: Int): List<Action> =
    if (npc is NPC.Hostile) {
        val newHealth = npc.health - damage
        if (newHealth <= 0) {
            listOf(
                sendText(message("game.slay-someone", npc.name, npc.name)),
                EntityAction(Remove, room, npc.left())
            ) + npc.items.map { EntityAction(Create, room, it.right()) }
        } else {
            listOf(
                sendText(message("game.hp-left", npc.name, newHealth)),
                HealthAction((room to npc).right(), -damage)
            ).let {
                it + if (this is Normal) {
                    listOfNotNull(
                        sendText(message("game.npc-hostile", npc.name, npc.nextAttackTimePoint().secondsRemaining())),
                        MasterCommandAction(
                            "hit \$player \$room $damage \$npc".replaceNPC(npc).replacePlayer(this).replaceRoom(room),
                            Duration.ofMillis(npc.nextAttackTimePoint().millisRemaining())
                        )
                    )
                } else {
                    emptyList()
                }
            }
        }
    } else {
        emptyList()
    }




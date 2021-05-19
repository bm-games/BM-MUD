package net.bmgames.game.action

import arrow.core.Either
import arrow.optics.dsl.index
import arrow.optics.typeclasses.Index
import net.bmgames.atIndex
import net.bmgames.state.model.*

/**
 * An action which sets the cooldown of a player / NPC.
 *
 *
 * @param target target which's hp will be updated, if a player is targeted, the type is just Player.Normal,
 * if the target is a hostile NPC, a pair of the NPC's room and the NPC is necessary.
 * @param timepoint the amount, the target's hp shall be decreased/increased with.
 *
 * @constructor creates a complete health action
 */
data class LastHitAction(val target: Either<Player.Normal, Pair<Room, NPC.Hostile>>, val timepoint: Long) :
    Update() {

    /**
     * Updates the gamestate based on the action.
     *
     * @param game the game the action is performed in
     * @return returns the new gamestate
     */
    override fun update(game: Game): Game =
        target.fold(
            { player ->
                Game.onlinePlayers.atIndex(player.ingameName)
                    .compose(Player.normal.lastHit)
                    .set(game, timepoint)
            },
            { (room, npc) ->
                Game.rooms.atIndex(room.name)
                    .compose(Room.npcs.index(Index.map(), npc.name))
                    .compose(NPC.hostile.lastDamageDealt)
                    .set(game, timepoint)
            }
        )
}

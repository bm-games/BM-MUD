package net.bmgames.game.action

import arrow.core.Either
import arrow.optics.dsl.index
import arrow.optics.typeclasses.Index
import net.bmgames.atIndex
import net.bmgames.state.model.*
/**
 * An action which creates and removes entities from rooms.
 *
 *
 * @param target target which's hp will be updated, if a player is targeted, the type is just Player.Normal,
 * if the target is a hostile NPC, a pair of the NPC's room and the NPC is necessary.
 * @param delta the amount, the target's hp shall be decreased/increased with.
 *
 * @constructor creates a complete health action
 */
data class HealthAction(val target: Either<Player.Normal, Pair<Room, NPC.Hostile>>, val delta: Int) : Update() {

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
                    .compose(Player.normal.healthPoints)
                    .modify(game) { it + delta }
            },
            { (room, npc) ->
                Game.rooms.atIndex(room.name)
                    .compose(Room.npcs.index(Index.map(), npc.name))
                    .compose(NPC.hostile.health)
                    .modify(game) { it + delta }
            }
        )
}

package net.bmgames.game.action

import arrow.core.Either
import arrow.optics.dsl.index
import arrow.optics.typeclasses.Index
import net.bmgames.atIndex
import net.bmgames.state.model.*

data class HealthAction(val target: Either<Player.Normal, Pair<Room, NPC.Hostile>>, val delta: Int) : Update() {

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

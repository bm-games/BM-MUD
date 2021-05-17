package net.bmgames.game.action

import net.bmgames.atIndex
import net.bmgames.state.model.*

data class MoveAction(val player: Player.Normal, val from: Room, val to: Room) : Update() {
    override fun update(game: Game): Game =
        if (game.rooms.containsKey(to.name))
            Game.onlinePlayers.atIndex(player.ingameName)
                .compose(Player.normal)
                .modify(game) {
                    it.copy(room = to.name, visitedRooms = it.visitedRooms + to.name)
                }
        else game
}

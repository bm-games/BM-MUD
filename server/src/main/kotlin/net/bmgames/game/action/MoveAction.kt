package net.bmgames.game.action

import net.bmgames.atIndex
import net.bmgames.state.model.*
/**
 * An action which gives moves the given player into another room.
 *
 *
 * @param player the player who gets moved.
 * @param from the room the player is in.
 * @param to the room the player is moved to.
 *
 * @constructor creates a complete move action
 */
data class MoveAction(val player: Player.Normal, val from: Room, val to: Room) : Update() {
    /**
     * Updates the gamestate based on the action. The targeted player should be in a new room.
     *
     * @param game the game the action is performed in
     * @return returns the new gamestate
     */
    override fun update(game: Game): Game =
        if (game.rooms.containsKey(to.name))
            Game.onlinePlayers.atIndex(player.ingameName)
                .compose(Player.normal)
                .modify(game) {
                    it.copy(room = to.name, visitedRooms = it.visitedRooms + to.name)
                }
        else game
}

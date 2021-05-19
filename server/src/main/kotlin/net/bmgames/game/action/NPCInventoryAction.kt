package net.bmgames.game.action

import net.bmgames.atIndex
import net.bmgames.state.model.*

/**
 * An action which updates a npcs inventory.
 *
 *
 * @param player the player whose inventory should be updated.
 * @param inventory the new inventory of the targeted player.
 *
 * @constructor creates a complete health action
 */
data class NPCInventoryAction(val npc: NPC, val room: Room, val items: List<Item>) : Update() {

    /**
     * Updates the gamestate based on the action.
     *
     * @param game the game the action is performed in
     * @return returns the new gamestate
     */
    override fun update(game: Game): Game =
        Game.rooms.atIndex(room.name)
            .compose(Room.npcs.atIndex(npc.name))
            .modify(game) {
                when(it){
                    is NPC.Friendly -> it.copy(items = items)
                    is NPC.Hostile -> it.copy(items = items)
                }
            }

}

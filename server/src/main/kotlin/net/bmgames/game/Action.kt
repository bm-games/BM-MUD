package net.bmgames.game

interface Action {
    fun updateGameState(state: GameState): GameState
}

data class MoveAction(val player: Player, val destination: Room) : Action {
    override fun updateGameState(state: GameState): GameState {
        TODO("Not yet implemented")
    }
}

data class MessageAction(val recipient: Player, val message: String) : Action {
    override fun updateGameState(state: GameState): GameState {
        TODO("Not yet implemented")
    }
}

data class AddItemAction(val player: Player, val item: Item) : Action {
    override fun updateGameState(state: GameState): GameState {
        TODO("Not yet implemented")
    }
}

data class KickPlayerAction(val player: Player) : Action {
    override fun updateGameState(state: GameState): GameState {
        TODO("Not yet implemented")
    }
}

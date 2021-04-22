package net.bmgames.game


interface Command {
    val name: String
    fun getAction(state: GameState): List<Action>
}

sealed class MasterCommand(override val name: String) : Command
class MasterSayCommand(val message: String) : MasterCommand("say") {
    override fun getAction(state: GameState): List<Action> {
        TODO("Not yet implemented")
    }
}

class MasterWhisperCommand(val recipient: String, val message: String) : MasterCommand("whisper") {
    override fun getAction(state: GameState): List<Action> {
        TODO("Not yet implemented")
    }
}

data class KickCommand(val player: String) : MasterCommand("kick") {
    override fun getAction(state: GameState): List<Action> {
        TODO("Not yet implemented")
    }
}


sealed class PlayerCommand(override val name: String) : Command
class PlayerSayCommand(val message: String) : PlayerCommand("say") {
    override fun getAction(state: GameState): List<Action> {
        TODO("Not yet implemented")
    }
}

class PlayerWhisperCommand(val recipient: Player, val message: String) : PlayerCommand("whisper") {
    override fun getAction(state: GameState): List<Action> {
        TODO("Not yet implemented")
    }
}

data class MoveCommand(val direction: String) : PlayerCommand("move") {
    override fun getAction(state: GameState): List<Action> {
        TODO("Not yet implemented")
    }
}

data class LookCommand(val target: String) : PlayerCommand("look") {
    override fun getAction(state: GameState): List<Action> {
        TODO("Not yet implemented")
    }
}

data class PickupCommand(val target: String) : PlayerCommand("pickup") {
    override fun getAction(state: GameState): List<Action> {
        TODO("Not yet implemented")
    }
}



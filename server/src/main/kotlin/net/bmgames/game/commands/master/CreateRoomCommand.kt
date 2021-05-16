package net.bmgames.game.commands.master

import arrow.core.Either
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class CreateRoomCommand : MasterCommand("createroom") {
    //rooms have to be implemented first to be able to create a command
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

package net.bmgames.game.commands.player

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.game.action.Action
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

/**
 * eg. move north
 * */
class MoveCommand : PlayerCommand("move") {

    enum class Direction { NORTH, EAST, SOUTH, WEST }
    val direction: Direction by argument(help = "The direction you want to move to").enum()

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Rooms not yet implemented")
        /*val playerRoom : Room
        when(direction){
            Direction.NORTH -> game. Logik zur Berechnung der Räume
            Direction.EAST -> game. Logik zur Berechnung der Räume
            Direction.WEST -> game. Logik zur Berechnung der Räume
            Direction.SOUTH -> game. Logik zur Berechnung der Räume*/
    }
}

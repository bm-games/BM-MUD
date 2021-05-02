package net.bmgames.game.commands

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.game.action.*
import net.bmgames.game.action.MessageAction
import net.bmgames.game.message.Message
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player


/**
 * @see [https://ajalt.github.io/clikt/]
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




class SayCommand :PlayerCommand("say") {

    val message: String by argument()
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        try {
            Either.Right(listOf<Action>(MessageAction(player, Message.Text(message))))
        } catch (e: Error) {
            Either.Left("Couldn't use say function")
        }
}

class WhisperCommand :PlayerCommand("whisper") {
    val target: String by argument(help = "The player you want to whisper to")
    val message: String by argument(help = "The message you want to send")
        .multiple(true)
        .transformAll { it.joinToString(" ") }
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class HitCommand :PlayerCommand("hit") {
    val target: String by argument(help = "The target you want to hit")
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Required functions not yet implemented")
        /*val returnList = mutableListOf<Action>()
        val targetPlayer : Player.Normal? = getPlayerByName(target)
        if(targetPlayer != null){
            returnList.add(HealthAction(targetPlayer, player.damage))
            returnList.add(MessageAction(targetPlayer, Message.Text("U got hit for ${player.damage} by ${player.ingameName}")))
            returnList.add(MessageAction(player, Message.Text("U hit ${targetPlayer.ingameName} for ${player.damage}")))
            return Either.Right(returnList)
        }
        return Either.Left("Either the player with this name is offline or doesn't exist")*/

    }
}

class LookCommand :PlayerCommand("look") {
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class InspectCommand :PlayerCommand("inspect") {
    val target: String by argument(help = "The target you want to inspect")
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class InventoryCommand :PlayerCommand("inventory") {
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class PickupCommand :PlayerCommand("pickup") {
    val target: String by argument(help = "The item you want to pickup")
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class DropItemCommand :PlayerCommand("drop") {
    val target: String by argument(help = "The item you want to drop")
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class UseItemCommand :PlayerCommand("use") {
    val target: String by argument(help = "The item you want to drop")
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}


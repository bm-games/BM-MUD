package net.bmgames.action

import net.bmgames.game.state.Game

interface Update : Action {
    fun update(game : Game) : Game
}
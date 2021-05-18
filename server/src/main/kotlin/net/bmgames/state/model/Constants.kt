package net.bmgames.state.model

/**
 * Default attack speed of an hostile NPC
 * */
const val ATTACK_COOLDOWN = 10L * 1000L

/**
 * In this time range the player can hit [Clazz.attackSpeed] times
 * */
const val HIT_TIMEFRAME = 20L * 1000L

/**
 * How many normal items can be hodled by a player
 * */
const val INVENTORY_SIZE = 10


fun Long.secondsRemaining(): Int = (this - System.currentTimeMillis()).toInt()
fun Float.toRelativePercent(): Int = ((this - 1) * 100).toInt()

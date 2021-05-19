package net.bmgames.state.model

import kotlin.math.max

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


fun Long.millisRemaining(): Long = max(this - System.currentTimeMillis(), 0)
fun Long.secondsRemaining(): Float = millisRemaining() / 1000f
fun Float.toRelativePercent(): Int = ((this - 1) * 100).toInt()

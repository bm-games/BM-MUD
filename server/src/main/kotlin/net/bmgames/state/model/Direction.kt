package net.bmgames.state.model

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun getOpposite(): Direction = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }
}

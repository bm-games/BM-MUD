package net.bmgames.game.message

import kotlinx.serialization.Serializable
import net.bmgames.game.message.Tile.Type.NotVisited
import net.bmgames.game.message.Tile.Type.Visited
import net.bmgames.state.model.Direction.*
import net.bmgames.state.model.Room

/**
 * The radius in which the player sees the visited rooms around him.
 * */
private const val MAP_RADIUS: Int = 1

/**
 * The width of the map. Calculated from [MAP_RADIUS]
 * */
private const val MAP_WIDTH: Int = MAP_RADIUS * 2 + 1

/**
 * The height of the map. Calculated from [MAP_RADIUS]
 * */
private const val MAP_HEIGHT: Int = MAP_RADIUS * 2 + 1


@Serializable
data class RoomMap(
    val tiles: List<List<Tile?>>
)

@Serializable
data class Tile(
    val north: Boolean,
    val east: Boolean,
    val south: Boolean,
    val west: Boolean,
    val type: Type
) {
    enum class Type { Visited, NotVisited }
}

fun Set<String>.toMap(getRoom: (String) -> Room?, currentRoom: Room): RoomMap {
    val map: MutableList<MutableList<Tile?>> =
        (0 until MAP_WIDTH).mapTo(mutableListOf()) {
            (0 until MAP_HEIGHT).mapTo(mutableListOf()) { null }
        }
    map.addRooms(MAP_RADIUS, MAP_RADIUS, this, { it?.let(getRoom) }, currentRoom)
    return RoomMap(map)
}

internal fun MutableList<MutableList<Tile?>>.addRooms(
    x: Int,
    y: Int,
    visitedRooms: Set<String>,
    getRoom: (String?) -> Room?,
    room: Room,
): Unit {
    if (x !in 0 until MAP_WIDTH || y !in 0 until MAP_HEIGHT) {
        return
    } else if (this[x][y] != null) {
        return
    } else if (!visitedRooms.contains(room.name)) {
        this[x][y] = with(room) {
            Tile(
                north = neighbours[NORTH] != null,
                west = neighbours[WEST] != null,
                east = neighbours[EAST] != null,
                south = neighbours[SOUTH] != null,
                type = NotVisited
            )
        }
    } else {
        this[x][y] = with(room) {
            Tile(
                north = neighbours[NORTH] != null,
                west = neighbours[WEST] != null,
                east = neighbours[EAST] != null,
                south = neighbours[SOUTH] != null,
                type = Visited
            )
        }
        getRoom(room.neighbours[NORTH])?.also { addRooms(x, y - 1, visitedRooms, getRoom, it) }
        getRoom(room.neighbours[WEST])?.also { addRooms(x - 1, y, visitedRooms, getRoom, it) }
        getRoom(room.neighbours[EAST])?.also { addRooms(x + 1, y, visitedRooms, getRoom, it) }
        getRoom(room.neighbours[SOUTH])?.also { addRooms(x, y + 1, visitedRooms, getRoom, it) }
    }
}

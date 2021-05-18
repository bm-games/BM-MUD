package net.bmgames.game.message

import net.bmgames.game.message.Tile.Type.*
import net.bmgames.state.model.Direction.*
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Room
import kotlin.math.max


/**
 * The radius in which the player sees the visited rooms around him.
 * */
private const val MAP_RADIUS: Int = 2

/**
 * The width of the map. Calculated from [MAP_RADIUS]
 * */
private const val MAP_WIDTH: Int = MAP_RADIUS * 2 + 1

/**
 * The width of the map in its biggest form
 * */
private const val MAX_MAP_WIDTH: Int = 16

/**
 * Helper class to transfer the room graph to a grid.
 *
 * @property visitedRooms All rooms in which the player has been. Is ignored for master
 * @property getPlayers A getter for all players in a certain room
 * @property getRoom Access to all rooms by name
 * @property asMaster If the map should be generated for a master (whole map) or just the player
 * */
class MapBuilder(
    private val visitedRooms: Set<String>,
    val getPlayers: (Room) -> Set<String>,
    val getRoom: (String?) -> Room?,
    private val asMaster: Boolean,
) {

    private lateinit var map: MutableList<MutableList<Tile?>>

    /**
     * Extracts the needed parts out of a game
     *
     * @param player The player for which this map is generated
     * */
    constructor(game: Game, visitedRooms: Set<String>, player: Player) : this(
        visitedRooms,
        { room ->
            game.onlinePlayers.mapNotNullTo(mutableSetOf()) { (_, it) ->
                if (it is Player.Normal && it.room == room.name) it.ingameName else null
            }
        },
        { it?.let(game::getRoom) },
        player is Player.Master
    )

    /**
     * Builds the map from the current room.
     * If this is run for a master, the whole map is converted.
     * Otherwise just [MAP_RADIUS] around the start room
     *
     * @param currentRoom The start room
     * */
    fun build(currentRoom: Room): RoomMap {
        val width = if (asMaster) MAX_MAP_WIDTH else MAP_WIDTH
        val radius = width / 2
        map = (0 until width).mapTo(mutableListOf()) {
            (0 until width).mapTo(mutableListOf()) { null }
        }
        addRooms(radius, radius, currentRoom)
        if (!asMaster) {
            map[radius][radius] = map[radius][radius]
                ?.copy(players = getPlayers(currentRoom), type = Current)
        }
        val startY = map.minOf { it -> it.indexOfFirst { it != null }.let { if(it == -1) radius else it } }
        val endY = map.maxOf { it -> it.indexOfLast { it != null }.let { if(it == -1) radius else it } }
        return map.mapNotNull { cols ->
            if (cols.filterNotNull().isEmpty()) null
            else cols.subList(startY, endY + 1)
        }.let(::RoomMap)
    }


    /**
     * Adds one room at a time and goes into the neighbours recursively
     *
     * @param x Current x coord
     * @param y Current y coord
     * @param room Current room
     * */
    private fun addRooms(x: Int, y: Int, room: Room): Unit {
        if (!asMaster && (x !in 0 until MAP_WIDTH || y !in 0 until MAP_WIDTH)) {
            return
        } else if (map[x][y] != null) {
            return
        } else if (!asMaster && !visitedRooms.contains(room.name)) {
            map[x][y] = with(room) {
                Tile(
                    north = neighbours[NORTH] != null,
                    west = neighbours[WEST] != null,
                    east = neighbours[EAST] != null,
                    south = neighbours[SOUTH] != null,
                    players = emptySet(),
                    type = NotVisited
                )
            }
        } else {
            map[x][y] = with(room) {
                Tile(
                    north = neighbours[NORTH] != null,
                    west = neighbours[WEST] != null,
                    east = neighbours[EAST] != null,
                    south = neighbours[SOUTH] != null,
                    npcs = if (asMaster) room.npcs.keys else emptySet(),
                    items = if (asMaster) room.items.map { it.name } else emptyList(),
                    players = if (asMaster) getPlayers(room) else emptySet(),
                    type = Visited
                )
            }
            getRoom(room.neighbours[NORTH])?.also { addRooms(x, y - 1, it) }
            getRoom(room.neighbours[WEST])?.also { addRooms(x - 1, y, it) }
            getRoom(room.neighbours[EAST])?.also { addRooms(x + 1, y, it) }
            getRoom(room.neighbours[SOUTH])?.also { addRooms(x, y + 1, it) }
        }
    }


}

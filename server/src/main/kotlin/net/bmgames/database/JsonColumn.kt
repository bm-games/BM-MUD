package net.bmgames.database

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table

class JsonColumn<T : Any>(
    private val deserialize: (String) -> T,
    private val serialize: (Any?) -> String?
) : ColumnType() {
    override fun sqlType() = "JSON"
    override fun valueFromDB(value: Any): T = deserialize(value.toString())
    override fun valueToDB(value: Any?): Any? = serialize(value)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Any> Table.json(
    name: String,
    serializer: KSerializer<T>
): Column<T> =
    registerColumn(
        name,
        JsonColumn(
            { json -> Json.decodeFromString(serializer, json) },
            { value ->
                when (value) {
                    is T -> Json.encodeToString(serializer, value)
                    else -> null
                }
            })
    )


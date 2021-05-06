package net.bmgames.state.model

open class IdEntity {
    var id: Int = 0
}

fun <T: IdEntity> T.setId(id: Int) = apply { this.id = id }

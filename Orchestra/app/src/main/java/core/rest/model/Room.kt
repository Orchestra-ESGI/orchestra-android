package core.rest.model

import java.io.Serializable

data class Room(
        var _id: String? = null,
        var name: String? = null
) : Serializable

data class ListRoom(
        val rooms: List<Room>
) : Serializable
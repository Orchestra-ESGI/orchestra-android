package core.rest.model.device

import core.rest.model.*
import java.io.Serializable

data class Device(
        var _id: String? = null,
        var name: String? = null,
        var room: Room? = null,
        var background_color: String? = null,
        var manufacturer: String? = null,
        var model: String? = null,
        var isOn: Boolean? = null,
        var is_reachable: Boolean? = null,
        var type: DeviceType? = null,
        var actions: Actions? = null,
        var friendly_name: String? = null
) : Serializable

package core.rest.model.hubConfiguration

import core.rest.model.Actions
import java.io.Serializable

data class HubAccessoryConfiguration(
        var name: String?,
        var room_name: String?,
        var background_color: String?,
        var manufacturer: String?,
        var model: String?,
        var isOn: Boolean?,
        var is_reachable: Boolean?,
        var type: HubAccessoryType?,
        var actions: Actions?,
        var friendly_name: String? = ""
) : Serializable

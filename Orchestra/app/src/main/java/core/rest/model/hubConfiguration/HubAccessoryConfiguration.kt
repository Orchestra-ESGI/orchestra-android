package core.rest.model.hubConfiguration

import core.rest.model.Actions
import java.io.Serializable

data class HubAccessoryConfiguration(
        var name: String? = null,
        var room_name: String? = null,
        var background_color: String? = null,
        var manufacturer: String? = null,
        var model: String? = null,
        var isOn: Boolean? = null,
        var is_reachable: Boolean? = null,
        var type: HubAccessoryType? = null,
        var actions: Actions? = null,
        var friendly_name: String? = null
) : Serializable

data class ListHubAccessoryConfigurationToDelete(
        var friendly_names: List<String>
) : Serializable

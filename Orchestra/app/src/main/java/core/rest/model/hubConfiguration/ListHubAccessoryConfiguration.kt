package core.rest.model.hubConfiguration

import java.io.Serializable

data class ListHubAccessoryConfiguration (
    var devices: List<Device>
) : Serializable
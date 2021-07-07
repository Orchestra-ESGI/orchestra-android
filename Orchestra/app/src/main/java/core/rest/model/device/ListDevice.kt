package core.rest.model.device

import java.io.Serializable

data class ListDevice (
    var devices: List<Device>
) : Serializable
package core.rest.model

import core.rest.model.hubConfiguration.HubAccessoryType
import java.io.Serializable

class SupportedDeviceInformations (
    val name : String,
    val manufacturer : String,
    val model : String,
    val image : String?,
    val documentation : String?,
    val type : String?
) : Serializable
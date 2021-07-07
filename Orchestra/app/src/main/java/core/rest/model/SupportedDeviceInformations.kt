package core.rest.model

import java.io.Serializable

class SupportedDeviceInformations (
    val name : String,
    val manufacturer : String,
    val model : String,
    val image : String?,
    val documentation : String?,
    val type : String?
) : Serializable
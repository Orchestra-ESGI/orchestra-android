package core.rest.model

import java.io.Serializable

class SupportedDevices (
    var brand : String,
    var devices: ArrayList<SupportedDeviceInformations>
) : Serializable
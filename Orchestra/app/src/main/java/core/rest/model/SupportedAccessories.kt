package core.rest.model

import java.io.Serializable

class SupportedAccessories (
    var brand : String,
    var devices: ArrayList<SupportedDeviceInformations>
) : Serializable
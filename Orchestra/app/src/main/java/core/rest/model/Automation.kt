package core.rest.model

import core.rest.model.device.DeviceType
import java.io.Serializable

data class Automation(
        var _id : String? = null,
        var name : String,
        var color : String,
        var description : String,
        var trigger : Trigger,
        var targets: List<ActionsToSet>
) : Serializable

data class Trigger(
        var type : DeviceType,
        var friendly_name : String,
        var actions : ActionsToSetIn
) : Serializable

data class ListAutomation(
        var automations : List<Automation>
) : Serializable
package core.rest.model

import core.rest.model.hubConfiguration.HubAccessoryType
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
        var type : HubAccessoryType,
        var friendly_name : String,
        var actions : ActionsToSetIn
) : Serializable

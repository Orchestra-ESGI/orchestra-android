package core.rest.model.hubConfiguration

import android.transition.Slide
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import core.rest.model.*
import java.io.Serializable

data class Device(
        var _id: String? = null,
        var name: String? = null,
        var room: Room? = null,
        var background_color: String? = null,
        var manufacturer: String? = null,
        var model: String? = null,
        var isOn: Boolean? = null,
        var is_reachable: Boolean? = null,
        var type: HubAccessoryType? = null,
        var actions: Actions? = null,
        var friendly_name: String? = null
) : Serializable

package core.rest.model.hubConfiguration

import android.transition.Slide
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import core.rest.model.Actions
import core.rest.model.ColorAction
import core.rest.model.DeviceState
import core.rest.model.SliderAction
import java.io.Serializable

data class HubAccessoryConfiguration(
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

data class ListHubAccessoryConfigurationToDelete(
        var friendly_names: List<String>
) : Serializable

data class Room(
        var _id: String? = null,
        var name: String? = null
) : Serializable

data class ListRoom(
        val rooms: List<Room>
) : Serializable

fun toSerialize(map: ArrayList<LinkedTreeMap<String, Any>>) : List<HubAccessoryConfiguration> {

        var listHubAccessoryConfiguration : ArrayList<HubAccessoryConfiguration> = ArrayList()

        map.forEach {
                val id = it["_id"] as? String
                val friendlyName = it["friendly_name"] as? String
                val name = it["name"] as? String

                var roomMap = it["room"] as LinkedTreeMap<String, Any>
                val room = Room()
                room._id = roomMap["_id"] as String
                room.name = roomMap["name"] as String

                val backgroundColor = it["background_color"] as? String
                val manufacturer = it["manufacturer"] as? String
                var model = it["model"] as? String
                var isOn = it["isOn"] as? Boolean
                var isReachable = it["is_reachable"] as? Boolean

                val typeMap = it["type"] as String
                var type : HubAccessoryType
                when(typeMap) {
                        "lightbulb" -> type = HubAccessoryType.lightbulb
                        "statelessProgrammableSwitch" -> type = HubAccessoryType.statelessProgrammableSwitch
                        "switch" -> type = HubAccessoryType.switch
                        "occupancy" -> type = HubAccessoryType.occupancy
                        "sensor" -> type = HubAccessoryType.sensor
                        else -> type = HubAccessoryType.unknown
                }

                var actionsMap = it["actions"] as LinkedTreeMap<String, Any>
                var actions = Actions()
                if (actionsMap["state"] == true) {
                        actions.state = DeviceState.on
                } else if (actionsMap["state"] == false) {
                        actions.state = DeviceState.off
                } else {
                        actions.state = Gson().fromJson(actionsMap["state"].toString(), DeviceState::class.java)
                }


                actions.brightness = Gson().fromJson(actionsMap["brightness"].toString(),SliderAction::class.java)
                val color = actionsMap["color"] as? LinkedTreeMap<String, Any>
                val colorAction = if (color != null) ColorAction(hex = color["hex"]!! as String) else null
                actions.color = colorAction
                actions.color_temp = Gson().fromJson(actionsMap["color_temp"].toString(),SliderAction::class.java)

                val hub = HubAccessoryConfiguration(
                        _id = id,
                        friendly_name = friendlyName,
                        name = name,
                        room = room,
                        background_color = backgroundColor,
                        manufacturer = manufacturer,
                        model = model,
                        isOn = isOn,
                        is_reachable = isReachable,
                        type = type,
                        actions = actions
                )
                listHubAccessoryConfiguration.add(hub)
        }

        return listHubAccessoryConfiguration
}
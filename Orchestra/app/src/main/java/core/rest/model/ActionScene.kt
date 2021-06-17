package core.rest.model

import java.io.Serializable

class ActionScene (
        var title: String = ""
        ) : Serializable

class SliderAction (
        var min_val: Int = 100,
        var max_val: Int = 500,
        var current_state: Int = 0
        ) : Serializable

data class ColorAction (
        var hex: String
        ) : Serializable

data class ActionsToSet(
        var friendly_name : String,
        var actions : ActionsToSetIn
) : Serializable

data class ActionsToSetIn(
        var state : String?,
        var brightness: Int?,
        var color: ColorAction?,
        var color_temp: Int?
) : Serializable

data class Actions (
        var state: DeviceState?,
        var brightness: SliderAction?,
        var color: ColorAction?,
        var color_temp: SliderAction?
        ) : Serializable

enum class DeviceState {
        Toggle,
        ON,
        OFF
}
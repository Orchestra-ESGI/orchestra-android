package utils

import core.rest.model.device.Device

interface OnActionLongClicked {
    fun actionLongClicked(device : Device, position : Int, isAction : Boolean)
}
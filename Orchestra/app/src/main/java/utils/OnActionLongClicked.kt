package utils

import core.rest.model.hubConfiguration.Device

interface OnActionLongClicked {
    fun actionLongClicked(device : Device, position : Int, isAction : Boolean)
}
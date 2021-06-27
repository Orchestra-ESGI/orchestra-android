package utils

import core.rest.model.hubConfiguration.HubAccessoryConfiguration

interface OnActionLongClicked {
    fun actionLongClicked(device : HubAccessoryConfiguration, position : Int, isAction : Boolean)
}
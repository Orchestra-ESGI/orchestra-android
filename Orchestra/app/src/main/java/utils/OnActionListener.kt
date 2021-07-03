package utils

import core.rest.model.hubConfiguration.HubAccessoryConfiguration

interface OnActionListener {
    fun onLongPressToDelete(id : String)
    fun onClickToLaunch(id : String)
}
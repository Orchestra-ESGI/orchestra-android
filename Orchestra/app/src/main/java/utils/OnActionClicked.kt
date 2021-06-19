package utils

import core.rest.model.SceneActionsName
import core.rest.model.hubConfiguration.HubAccessoryConfiguration

interface OnActionClicked {
    fun actionClicked(action: SceneActionsName, position: Int)
}
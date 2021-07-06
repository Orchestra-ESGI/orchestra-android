package utils

import core.rest.model.SceneActionsName

interface OnActionClicked {
    fun actionClicked(action: SceneActionsName, position: Int)
}
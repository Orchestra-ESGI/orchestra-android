package utils

interface OnSceneListener {
    fun onLongPressToDelete(id : String, sender : String)
    fun onClickToLaunch(id : String, sender : String)
}
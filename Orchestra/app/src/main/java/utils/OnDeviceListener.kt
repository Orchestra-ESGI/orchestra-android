package utils

interface OnDeviceListener {
    fun onLongPressToDeleteDevice(friendlyNameToDelete : HashMap<String, List<String>>)
}
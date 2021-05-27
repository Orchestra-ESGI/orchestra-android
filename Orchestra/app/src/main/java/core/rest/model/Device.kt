package core.rest.model

import java.io.Serializable

class Device (
    val id: String?,
    val name: String?,
    val roomName: String?,
    val backgroundColor: String?,
    val manufacturer: String?,
    val serialNumber: String?,
    val model: String?,
    val isOn: Boolean?,
    val isFav: Boolean?,
    val isReachable: Boolean?,
    val version: String?
        ) : Serializable
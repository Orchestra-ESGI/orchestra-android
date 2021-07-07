package core.rest.services

import core.rest.model.ActionsToSet
import core.rest.model.ListRoom
import core.rest.model.SupportedDevices
import core.rest.model.device.*
import retrofit2.Call
import retrofit2.http.*

interface DeviceService {
    @GET("/device/supported")
    fun getSupportedAccessories() : Call<List<SupportedDevices>>

    @GET("device/all")
    // fun getAllDevices() : Call<HashMap<String, Any>>
    fun getAllDevices() : Call<ListDevice>

    @POST("/device/action")
    fun sendDeviceAction(@Body actionsToSet: ActionsToSet) : Call<ListDevice>

    @POST("/device/reset")
    fun resetDevice() : Call<Device>

    @PATCH("/device")
    fun saveDevice(@Body device: Device): Call<Device>

    @HTTP(method = "DELETE", path = "/device", hasBody = true)
    fun deleteDevices(@Body friendly_name: HashMap<String, List<String>>): Call<HashMap<String, Any>>

    @GET("/room/all")
    fun getAllRooms() : Call<ListRoom>

    @POST("/room")
    fun addRoom(@Body body: HashMap<String, String>): Call<HashMap<String, Any>>
}
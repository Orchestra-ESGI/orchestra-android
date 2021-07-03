package core.rest.services

import core.rest.model.ActionsToSet
import core.rest.model.ListRoom
import core.rest.model.SupportedAccessories
import core.rest.model.hubConfiguration.*
import retrofit2.Call
import retrofit2.http.*

interface DeviceService {
    @GET("device/supported")
    fun getSupportedAccessories() : Call<List<SupportedAccessories>>

    @GET("device/all")
    // fun getAllDevices() : Call<HashMap<String, Any>>
    fun getAllDevices() : Call<ListHubAccessoryConfiguration>

    @POST("device/action")
    fun sendDeviceAction(@Body actionsToSet: ActionsToSet) : Call<ListHubAccessoryConfiguration>

    @POST("device/reset")
    fun resetDevice() : Call<HubAccessoryConfiguration>

    @PATCH("device")
    fun saveDevice(@Body device: HubAccessoryConfiguration): Call<HubAccessoryConfiguration>

    @HTTP(method = "DELETE", path = "/device", hasBody = true)
    fun deleteDevices(@Body friendly_name: ListHubAccessoryConfigurationToDelete): Call<ListHubAccessoryConfigurationToDelete>

    @GET("room/all")
    fun getAllRooms() : Call<ListRoom>

    @POST("room")
    fun addRoom(@Body body: HashMap<String, String>): Call<HashMap<String, Any>>
}
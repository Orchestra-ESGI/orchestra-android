package core.rest.services

import core.rest.model.ActionsToSet
import core.rest.model.SupportedAccessories
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.ListHubAccessoryConfiguration
import retrofit2.Call
import retrofit2.http.*

interface DeviceService {
    @GET("device/supported")
    fun getSupportedAccessories() : Call<List<SupportedAccessories>>

    @GET("device/all")
    fun getAllDevices() : Call<ListHubAccessoryConfiguration>

    @POST("device/action")
    fun sendDeviceAction(@Body actionsToSet: ActionsToSet) : Call<ListHubAccessoryConfiguration>

    @POST("device/reset")
    fun resetDevice() : Call<HubAccessoryConfiguration>

    @POST("device/add")
    fun saveDevice(@Body device: HubAccessoryConfiguration): Call<HubAccessoryConfiguration>

    @DELETE("device/{id}")
    fun deleteDevice(@Path("id") friendly_name: String): Call<HubAccessoryConfiguration>
}
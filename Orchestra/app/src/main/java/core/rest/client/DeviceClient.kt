package core.rest.client

import android.util.Log
import androidx.lifecycle.MutableLiveData
import core.rest.model.*
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.ListHubAccessoryConfiguration
import core.rest.services.DeviceService
import core.rest.services.RootApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.Serializable

object DeviceClient {
    private var deviceServices: DeviceService? = getApi()
    private var mappingDevice : ListHubAccessoryConfiguration? = null
    var supportedDevices: MutableLiveData<List<SupportedAccessories>> = MutableLiveData()
    var deviceList: MutableLiveData<List<HubAccessoryConfiguration>> = MutableLiveData()

    private fun getApi(): DeviceService? {
        if (deviceServices == null) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(RootApiService.ROOT_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            deviceServices = retrofit.create(DeviceService::class.java)
        }
        return deviceServices
    }

    fun getAllDevices() {
        getApi()?.getAllDevices()
            ?.enqueue(object : Callback<ListHubAccessoryConfiguration> {
                override fun onResponse(
                    call: Call<ListHubAccessoryConfiguration>?,
                    response: Response<ListHubAccessoryConfiguration>?
                ) {
                    mappingDevice = response!!.body()
                    deviceList.value = mappingDevice!!.devices

                    Log.d("TestSuccess", response!!.body().toString())
                }

                override fun onFailure(call: Call<ListHubAccessoryConfiguration>?, t: Throwable?) {
                    Log.e("error", t?.message!!)
                }

            })
    }

    fun sendDeviceAction(device : ActionsToSet) {
        getApi()?.sendDeviceAction(device)?.enqueue(object : Callback<ListHubAccessoryConfiguration> {
            override fun onResponse(
                call: Call<ListHubAccessoryConfiguration>,
                response: Response<ListHubAccessoryConfiguration>
            ) {

                Log.d("Test Send Device", "OK")
            }

            override fun onFailure(call: Call<ListHubAccessoryConfiguration>, t: Throwable) {
                Log.d("Test Send Device", "NOK")
            }
        })
    }

    fun getSupportedAccessories() {
        getApi()?.getSupportedAccessories()
                ?.enqueue(object : Callback<List<SupportedAccessories>> {
                    override fun onResponse(
                            call: Call<List<SupportedAccessories>>?,
                            response: Response<List<SupportedAccessories>>?
                    ) {
                        supportedDevices.value = response!!.body()
                        Log.d("TestSuccess", response!!.body().toString())
                    }

                    override fun onFailure(call: Call<List<SupportedAccessories>>?, t: Throwable?) {
                        Log.e("error", t?.message!!)
                    }

                })
    }

    fun resetDevice() {
        getApi()?.resetDevice()?.enqueue(object : Callback<HubAccessoryConfiguration> {
            override fun onResponse(
                call: Call<HubAccessoryConfiguration>,
                response: Response<HubAccessoryConfiguration>
            ) {
                Log.d("Test Reset Device", "OK")
            }

            override fun onFailure(call: Call<HubAccessoryConfiguration>, t: Throwable) {
                Log.d("Test Reset Device", "NOK")
            }
        })
    }

    fun saveDevice(device : HubAccessoryConfiguration) {
        getApi()?.saveDevice(device)?.enqueue(object : Callback<HubAccessoryConfiguration> {
            override fun onResponse(
                    call: Call<HubAccessoryConfiguration>,
                    response: Response<HubAccessoryConfiguration>
            ) {
                Log.d("Test Send Device", "OK")
            }

            override fun onFailure(call: Call<HubAccessoryConfiguration>, t: Throwable) {
                Log.d("Test Send Device", "NOK")
            }
        })
    }

    fun deleteDevice(device : String) {
        getApi()?.deleteDevice(device)?.enqueue(object : Callback<HubAccessoryConfiguration> {
            override fun onResponse(
                    call: Call<HubAccessoryConfiguration>,
                    response: Response<HubAccessoryConfiguration>
            ) {
                Log.d("Test Delete Device", "OK")
            }

            override fun onFailure(call: Call<HubAccessoryConfiguration>, t: Throwable) {
                Log.d("Test Send Device", "NOK")
            }
        })
    }
}
package core.rest.client

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import core.rest.model.*
import core.rest.model.hubConfiguration.*
import core.rest.services.DeviceService
import core.rest.services.RootApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DeviceClient {
    private var deviceServices: DeviceService? = getApi()
    var supportedDevices: MutableLiveData<List<SupportedAccessories>> = MutableLiveData()
    var deviceList: MutableLiveData<List<HubAccessoryConfiguration>> = MutableLiveData()
    var roomList: MutableLiveData<ListRoom> = MutableLiveData()
    var apiError : MutableLiveData<ApiError> = MutableLiveData()

    private fun getApi(context: Context? = null): DeviceService? {
        if(context != null) {
            val sharedPref = context.getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE)
            val token = sharedPref.getString("Token", "")
            val okHttpClient = OkHttpClient.Builder().apply {
                addInterceptor(
                        Interceptor { chain ->
                            val builder = chain.request().newBuilder()
                            builder.header("Authorization", "Bearer $token")
                            builder.header("App-Key", "orchestra")
                            return@Interceptor chain.proceed(builder.build())
                        }
                )
            }.build()

            if (deviceServices == null) {
                val retrofit = Retrofit.Builder()
                        .baseUrl(RootApiService.ROOT_PATH)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build()
                deviceServices = retrofit.create(DeviceService::class.java)
            }
        }
        return deviceServices
    }

    fun getAllDevices(context: Context) {
        getApi(context)?.getAllDevices()
            ?.enqueue(object : Callback<ListHubAccessoryConfiguration> {
                override fun onResponse(
                    call: Call<ListHubAccessoryConfiguration>,
                    response: Response<ListHubAccessoryConfiguration>
                ) {
                    if (response.isSuccessful) {
                        deviceList.value = response.body()?.devices
                        Log.d("TestSuccess", response.body().toString())
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        apiError.value = ApiError(error = jObjError["error"].toString())
                    }

                }

                override fun onFailure(call: Call<ListHubAccessoryConfiguration>, t: Throwable?) {
                    apiError.value = ApiError(error = t?.message!!)
                    Log.e("error", t?.message!!)
                }

            })
    }

    fun sendDeviceAction(device : ActionsToSet, context: Context) {
        getApi(context)?.sendDeviceAction(device)?.enqueue(object : Callback<ListHubAccessoryConfiguration> {
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

    fun getSupportedAccessories(context: Context) {
        getApi(context)?.getSupportedAccessories()
                ?.enqueue(object : Callback<List<SupportedAccessories>> {
                    override fun onResponse(
                            call: Call<List<SupportedAccessories>>?,
                            response: Response<List<SupportedAccessories>>?
                    ) {
                        supportedDevices.value = response!!.body()
                        Log.d("TestSuccess", response.body().toString())
                    }

                    override fun onFailure(call: Call<List<SupportedAccessories>>?, t: Throwable?) {
                        Log.e("error", t?.message!!)
                    }

                })
    }

    fun resetDevice(context: Context) {
        getApi(context)?.resetDevice()?.enqueue(object : Callback<HubAccessoryConfiguration> {
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

    fun saveDevice(device : HubAccessoryConfiguration, context: Context) {
        getApi(context)?.saveDevice(device)?.enqueue(object : Callback<HubAccessoryConfiguration> {
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

    fun deleteDevices(device : ListHubAccessoryConfigurationToDelete, context: Context) {
        getApi(context)?.deleteDevices(device)?.enqueue(object : Callback<ListHubAccessoryConfigurationToDelete> {
            override fun onResponse(
                    call: Call<ListHubAccessoryConfigurationToDelete>,
                    response: Response<ListHubAccessoryConfigurationToDelete>
            ) {
                Log.d("Test Delete Device", "OK")
            }

            override fun onFailure(call: Call<ListHubAccessoryConfigurationToDelete>, t: Throwable) {
                Log.d("Test Send Device", "NOK")
            }
        })
    }

    fun getAllRoom(context: Context) {
        getApi(context)?.getAllRooms()?.enqueue(object : Callback<ListRoom> {
            override fun onResponse(
                    call: Call<ListRoom>,
                    response: Response<ListRoom>
            ) {
                if (response.isSuccessful) {
                    roomList.value = response.body()
                    Log.d("Test Room Get", "OK")
                } else {
                    Log.d("Test Room Get", "NOK")
                }

            }

            override fun onFailure(call: Call<ListRoom>, t: Throwable) {
                Log.d("Test Room Get", "FAILED")
            }
        })
    }
}
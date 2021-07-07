package core.rest.client

import android.content.Context
import core.rest.model.*
import core.rest.model.device.*
import core.rest.services.DeviceService
import core.rest.services.RootApiService
import core.utils.SingleLiveEvent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DeviceClient {
    private var deviceServices: DeviceService? = getApi()
    var supportedDevices: SingleLiveEvent<List<SupportedDevices>> = SingleLiveEvent()
    var deviceList: SingleLiveEvent<List<Device>> = SingleLiveEvent()
    var roomList: SingleLiveEvent<List<Room>> = SingleLiveEvent()

    private fun getApi(context: Context? = null): DeviceService? {
        if(context != null) {
            val okHttpClient = OkHttpClient.Builder().apply {
                addInterceptor(
                        Interceptor { chain ->
                            val builder = chain.request().newBuilder()
                            val sharedPref = context.getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE)
                            val token = sharedPref.getString("Token", "")
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
            ?.enqueue(object : Callback<ListDevice> {
                override fun onResponse(
                        call: Call<ListDevice>,
                        response: Response<ListDevice>
                ) {
                    if (response.isSuccessful) {
                        deviceList.value = response.body()?.devices
                    } else {
                        RootApiService.handleError(context, response.code())
                    }

                }

                override fun onFailure(call: Call<ListDevice>, t: Throwable?) {
                    RootApiService.handleError(context, 500)
                }

            })
    }

    fun sendDeviceAction(device : ActionsToSet, context: Context) {
        getApi(context)?.sendDeviceAction(device)?.enqueue(object : Callback<ListDevice> {
            override fun onResponse(
                    call: Call<ListDevice>,
                    response: Response<ListDevice>
            ) {
                if (!response.isSuccessful) {
                    RootApiService.handleError(context, response.code())
                }
            }

            override fun onFailure(call: Call<ListDevice>, t: Throwable) {
                RootApiService.handleError(context, 500)
            }
        })
    }

    fun getSupportedAccessories(context: Context) {
        getApi(context)?.getSupportedAccessories()
                ?.enqueue(object : Callback<List<SupportedDevices>> {
                    override fun onResponse(
                            call: Call<List<SupportedDevices>>,
                            response: Response<List<SupportedDevices>>
                    ) {
                        if (response.isSuccessful) {
                            supportedDevices.value = response.body()
                        } else {
                            RootApiService.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<List<SupportedDevices>>?, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun resetDevice(context: Context) {
        getApi(context)?.resetDevice()?.enqueue(object : Callback<Device> {
            override fun onResponse(
                    call: Call<Device>,
                    response: Response<Device>
            ) {
                if (!response.isSuccessful) {
                    RootApiService.handleError(context, response.code())
                }
            }

            override fun onFailure(call: Call<Device>, t: Throwable) {
                RootApiService.handleError(context, 500)
            }
        })
    }

    fun saveDevice(device : Device, context: Context) {
        getApi(context)?.saveDevice(device)?.enqueue(object : Callback<Device> {
            override fun onResponse(
                    call: Call<Device>,
                    response: Response<Device>
            ) {
                if(!response.isSuccessful) {
                    RootApiService.handleError(context, response.code())
                }
            }

            override fun onFailure(call: Call<Device>, t: Throwable) {
                RootApiService.handleError(context, 500)
            }
        })
    }

    fun deleteDevices(device : HashMap<String, List<String>>, context: Context) {
        getApi(context)?.deleteDevices(device)?.enqueue(object : Callback<HashMap<String, Any>> {
            override fun onResponse(
                    call: Call<HashMap<String, Any>>,
                    response: Response<HashMap<String, Any>>
            ) {
                if (!response.isSuccessful) {
                    RootApiService.handleError(context, response.code())
                }
            }

            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {
                RootApiService.handleError(context, 500)
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
                    val listRoom = response.body()
                    roomList.value = listRoom?.rooms
                } else {
                    RootApiService.handleError(context, response.code())
                }
            }

            override fun onFailure(call: Call<ListRoom>, t: Throwable) {
                RootApiService.handleError(context, 500)
            }
        })
    }

    fun addRoom(context: Context, room: String) {
        val body : HashMap<String, String> = HashMap()
        body["name"] = room
        getApi(context)?.addRoom(body)?.enqueue(object : Callback<HashMap<String, Any>> {
            override fun onResponse(
                call: Call<HashMap<String, Any>>,
                response: Response<HashMap<String, Any>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                } else {
                    RootApiService.handleError(context, response.code())
                }
            }

            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {
                RootApiService.handleError(context, 500)
            }
        })
    }
}
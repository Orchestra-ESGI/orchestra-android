package core.rest.client

import android.content.Context
import androidx.lifecycle.MutableLiveData
import core.rest.model.*
import core.rest.services.RootApiService
import core.rest.services.SceneServices
import core.utils.SingleLiveEvent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SceneClient {
    private var sceneServices: SceneServices? = getApi()
    var sceneList: SingleLiveEvent<List<Scene>> = SingleLiveEvent()
    var automationList: SingleLiveEvent<List<Automation>> = SingleLiveEvent()

    private fun getApi(context: Context? = null): SceneServices? {
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
            if (sceneServices == null) {
                val retrofit = Retrofit.Builder()
                        .baseUrl(RootApiService.ROOT_PATH)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build()
                sceneServices = retrofit.create(SceneServices::class.java)
            }
        }
        return sceneServices
    }

    fun getAllScene(context: Context) {
        getApi(context)?.getAllScenes()
            ?.enqueue(object : Callback<ListScene>{
                override fun onResponse(
                    call: Call<ListScene>,
                    response: Response<ListScene>
                ) {
                    if(response.isSuccessful) {
                        sceneList.value = response.body()!!.scenes
                    } else {
                        RootApiService.handleError(context, response.code())
                    }
                }

                override fun onFailure(call: Call<ListScene>?, t: Throwable?) {
                    RootApiService.handleError(context, 500)
                }

            })
    }

    fun getAllAutomation(context: Context) {
        getApi(context)?.getAllAutomation()
                ?.enqueue(object : Callback<ListAutomation>{
                    override fun onResponse(
                            call: Call<ListAutomation>,
                            response: Response<ListAutomation>
                    ) {
                        if (response.isSuccessful) {
                            automationList.value = response.body()?.automations
                        } else {
                            RootApiService.handleError(context, response.code())
                        }
                    }
                    override fun onFailure(call: Call<ListAutomation>?, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun addScene(scene : Scene, context: Context) {
        getApi(context)?.addScene(scene)
                ?.enqueue(object : Callback<Scene>{
                    override fun onResponse(
                            call: Call<Scene>,
                            response: Response<Scene>
                    ) {
                        if (!response.isSuccessful) {
                            RootApiService.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<Scene>?, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun deleteScenes(sceneIds : ListSceneToDelete, context: Context) {
        getApi(context)?.deleteScenes(sceneIds)
                ?.enqueue(object : Callback<ListSceneToDelete>{
                    override fun onResponse(
                            call: Call<ListSceneToDelete>,
                            response: Response<ListSceneToDelete>
                    ) {
                        if (!response.isSuccessful) {
                            RootApiService.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<ListSceneToDelete>?, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun launchScene(sceneId : String, context: Context) {
        getApi(context)?.launchScene(sceneId)
                ?.enqueue(object : Callback<Scene>{
                    override fun onResponse(
                            call: Call<Scene>,
                            response: Response<Scene>
                    ) {
                        if (!response.isSuccessful) {
                            RootApiService.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<Scene>, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun launchAutomation(automationId : String, context: Context) {
        getApi(context)?.launchAutomation(automationId)
                ?.enqueue(object : Callback<HashMap<String, Any>> {
                    override fun onResponse(
                            call: Call<HashMap<String, Any>>,
                            response: Response<HashMap<String, Any>>
                    ) {
                        if (!response.isSuccessful) {
                            RootApiService.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun updateScene(scene : Scene, context: Context) {
        getApi(context)?.updateScene(scene)
            ?.enqueue(object : Callback<Scene>{
                override fun onResponse(
                    call: Call<Scene>,
                    response: Response<Scene>
                ) {
                    if (!response.isSuccessful) {
                        RootApiService.handleError(context, response.code())
                    }
                }

                override fun onFailure(call: Call<Scene>?, t: Throwable?) {
                    RootApiService.handleError(context, 500)
                }

            })
    }

    fun saveAutomation(automation : Automation, context: Context) {
        getApi(context)?.addAutomation(automation)
                ?.enqueue(object : Callback<HashMap<String,Any>>{
                    override fun onResponse(
                            call: Call<HashMap<String,Any>>,
                            response: Response<HashMap<String,Any>>
                    ) {
                        if (!response.isSuccessful) {
                            RootApiService.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<HashMap<String,Any>>?, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun updateAutomation(automation: Automation, context: Context) {
        getApi(context)?.updateAutomation(automation)
                ?.enqueue(object : Callback<HashMap<String, Any>>{
                    override fun onResponse(
                            call: Call<HashMap<String, Any>>,
                            response: Response<HashMap<String, Any>>
                    ) {
                        if (!response.isSuccessful) {
                            RootApiService.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun deleteAutomations(idList : HashMap<String, List<String>>, context: Context) {
        getApi(context)?.deleteScenes(idList)
                ?.enqueue(object : Callback<HashMap<String, Any>>{
                    override fun onResponse(
                            call: Call<HashMap<String, Any>>,
                            response: Response<HashMap<String, Any>>
                    ) {
                        if (!response.isSuccessful) {
                            RootApiService.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }
}
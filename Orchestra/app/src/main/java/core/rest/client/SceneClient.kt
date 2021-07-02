package core.rest.client

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import core.rest.model.*
import core.rest.services.RootApiService
import core.rest.services.SceneServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SceneClient {
    private var sceneServices: SceneServices? = getApi()
    var sceneList: MutableLiveData<List<Scene>> = MutableLiveData()
    var automationList: MutableLiveData<List<Automation>> = MutableLiveData()

    private fun getApi(context: Context? = null): SceneServices? {
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

    fun getAllScene(context: Context?) {
        getApi(context)?.getAllScenes()
            ?.enqueue(object : Callback<ListScene>{
                override fun onResponse(
                    call: Call<ListScene>?,
                    response: Response<ListScene>?
                ) {
                    sceneList.value = response!!.body()!!.scenes
                    Log.d("TestSuccess", response.body().toString())
                }

                override fun onFailure(call: Call<ListScene>?, t: Throwable?) {
                    Log.e("error", t?.message!!)
                }

            })
    }

    fun getAllAutomation(context: Context?) {
        getApi(context)?.getAllAutomation()
                ?.enqueue(object : Callback<ListAutomation>{
                    override fun onResponse(
                            call: Call<ListAutomation>,
                            response: Response<ListAutomation>
                    ) {
                        if (response.isSuccessful) {
                            automationList.value = response.body()?.automations
                            Log.d("TestSuccess", response.body().toString())
                        } else {
                            Log.d("TestSuccess", response.body().toString())
                        }

                    }

                    override fun onFailure(call: Call<ListAutomation>?, t: Throwable?) {
                        Log.e("error", t?.message!!)
                    }

                })
    }

    fun addScene(scene : Scene, context: Context?) {
        getApi(context)?.addScene(scene)
                ?.enqueue(object : Callback<Scene>{
                    override fun onResponse(
                            call: Call<Scene>?,
                            response: Response<Scene>?
                    ) {
                        Log.d("TestSuccess", response!!.body().toString())
                    }

                    override fun onFailure(call: Call<Scene>?, t: Throwable?) {
                        Log.e("error", t?.message!!)
                    }

                })
    }

    fun deleteScenes(sceneIds : ListSceneToDelete, context: Context?) {
        getApi(context)?.deleteScenes(sceneIds)
                ?.enqueue(object : Callback<ListSceneToDelete>{
                    override fun onResponse(
                            call: Call<ListSceneToDelete>?,
                            response: Response<ListSceneToDelete>?
                    ) {
                        Log.d("TestSuccess", response!!.body().toString())
                    }

                    override fun onFailure(call: Call<ListSceneToDelete>?, t: Throwable?) {
                        Log.e("error", t?.message!!)
                    }

                })
    }

    fun launchScene(sceneId : String, context: Context?) {
        getApi(context)?.launchScene(sceneId)
                ?.enqueue(object : Callback<Scene>{
                    override fun onResponse(
                            call: Call<Scene>?,
                            response: Response<Scene>?
                    ) {
                        Log.d("TestSuccess", response!!.body().toString())
                    }

                    override fun onFailure(call: Call<Scene>?, t: Throwable?) {
                        Log.e("error", t?.message!!)
                    }

                })
    }

    fun updateScene(scene : Scene, context: Context?) {
        getApi(context)?.updateScene(scene)
            ?.enqueue(object : Callback<Scene>{
                override fun onResponse(
                    call: Call<Scene>?,
                    response: Response<Scene>?
                ) {
                    Log.d("TestSuccess", response!!.body().toString())
                }

                override fun onFailure(call: Call<Scene>?, t: Throwable?) {
                    Log.e("error", t?.message!!)
                }

            })
    }

    fun saveAutomation(automation : Automation, context: Context?) {
        getApi(context)?.addAutomation(automation)
                ?.enqueue(object : Callback<HashMap<String,Any>>{
                    override fun onResponse(
                            call: Call<HashMap<String,Any>>,
                            response: Response<HashMap<String,Any>>
                    ) {
                        if (response.isSuccessful) {
                            val res = response.body()
                            val erreur = res?.get("error") as? List<Room>
                            Log.d("Test Automation Save", "OK")
                        } else {
                            Log.d("Test Automation Save", "NOK")
                        }
                        Log.d("TestSuccess", response.body().toString())
                    }

                    override fun onFailure(call: Call<HashMap<String,Any>>?, t: Throwable?) {
                        Log.e("error", t?.message!!)
                    }

                })
    }
}
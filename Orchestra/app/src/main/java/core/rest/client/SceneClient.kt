package core.rest.client

import android.util.Log
import androidx.lifecycle.MutableLiveData
import core.rest.model.ListScene
import core.rest.model.ListSceneToDelete
import core.rest.model.hubConfiguration.ListHubAccessoryConfiguration
import core.rest.services.RootApiService
import core.rest.services.SceneServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import core.rest.model.Scene
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object SceneClient {
    private var sceneServices: SceneServices? = getApi()
    var sceneList: MutableLiveData<List<Scene>> = MutableLiveData()

    private fun getApi(): SceneServices? {
        if (sceneServices == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(RootApiService.ROOT_PATH)
                // .addConverterFactory(MoshiConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                .build()
            sceneServices = retrofit.create(SceneServices::class.java)
        }
        return sceneServices
    }

    fun getAllScene() {
        getApi()?.getAllScenes()
            ?.enqueue(object : Callback<ListScene>{
                override fun onResponse(
                    call: Call<ListScene>?,
                    response: Response<ListScene>?
                ) {
                    sceneList.value = response!!.body()!!.scenes
                    Log.d("TestSuccess", response!!.body().toString())
                }

                override fun onFailure(call: Call<ListScene>?, t: Throwable?) {
                    Log.e("error", t?.message!!)
                }

            })
    }

    fun addScene(scene : Scene) {
        getApi()?.addScene(scene)
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

    fun deleteScenes(sceneIds : ListSceneToDelete) {
        getApi()?.deleteScenes(sceneIds)
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

    fun launchScene(sceneId : String) {
        getApi()?.launchScene(sceneId)
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
}
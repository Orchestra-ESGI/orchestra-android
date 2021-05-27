package core.rest.client

import android.util.Log
import core.rest.services.RootApiService
import core.rest.services.SceneServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SceneClient {
    private var sceneServices: SceneServices? = getApi()

    private fun getApi(): SceneServices? {
        if (sceneServices == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(RootApiService.ROOT_PATH)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            sceneServices = retrofit.create(SceneServices::class.java)
        }
        return sceneServices
    }

    fun getAllScenes(idHouse : String) {
        getApi()?.getAllScenes(idHouse)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
            { result -> Log.d("success", result.toString()) },
            { error -> Log.d("error", error.message!!) }
        )
    }

    fun removeScene(scenesId : List<String>) {
        getApi()?.removeScene(scenesId)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
            { result -> Log.d("success", result.toString()) },
            { error -> Log.d("error", error.message!!) }
        )
    }
}
package core.rest.client

import android.util.Log
import core.rest.model.User
import core.rest.services.HouseServices
import core.rest.services.RootApiService
import core.rest.services.UserServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object HouseClient {
    private var houseServices: HouseServices? = getApi()

    private fun getApi(): HouseServices? {
        if (houseServices == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(RootApiService.ROOT_PATH)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            houseServices = retrofit.create(HouseServices::class.java)
        }
        return houseServices
    }

    fun getAllHouses() {
        getApi()?.getAllHouses()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
            { result -> Log.d("success", result.toString()) },
            { error -> Log.d("error", error.message!!) }
        )
    }

    fun removeScene(idHouse : String) {
        getApi()?.removeScene(idHouse)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
            { result -> Log.d("success", result.toString()) },
            { error -> Log.d("error", error.message!!) }
        )
    }
}
package core.rest.client

import android.util.Log
import core.rest.model.User
import core.rest.services.RootApiService
import core.rest.services.UserServices
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object UserClient {
    private var userServices: UserServices? = getApi()

    private fun getApi(): UserServices? {
        if (userServices == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(RootApiService.ROOT_PATH)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            userServices = retrofit.create(UserServices::class.java)
        }
        return userServices
    }

    fun getAllUsers() {
        getApi()?.getAllUsers()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
            { result -> Log.d("success", result.toString()) },
            { error -> Log.d("error", error.message!!) }
        )
    }

    fun removeUser(usersId : List<String>) {
        getApi()?.removeUser(usersId)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
            { result -> Log.d("success", result.toString()) },
            { error -> Log.d("error", error.message!!) }
        )
    }

    fun signIn(user: User) {
        getApi()?.signin(user)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
            { result -> Log.d("success", result.toString()) },
            { error -> Log.d("error", error.message!!) }
        )
    }

    fun login(user: User) {
        getApi()?.signin(user)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(
            { result -> Log.d("success", result.toString()) },
            { error -> Log.d("error", error.message!!) }
        )
    }
}
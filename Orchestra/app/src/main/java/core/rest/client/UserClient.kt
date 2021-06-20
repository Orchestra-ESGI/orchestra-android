package core.rest.client

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import core.rest.model.User
import core.rest.model.UserValid
import core.rest.services.RootApiService
import core.rest.services.SceneServices
import core.rest.services.UserServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object UserClient {
    private var userServices: UserServices? = getApi()
    var userValid: MutableLiveData<UserValid> = MutableLiveData()

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

    fun login(user: User) {
        getApi()?.login(user)
                ?.enqueue(object : Callback<UserValid> {
                    override fun onResponse(
                            call: Call<UserValid>,
                            response: Response<UserValid>
                    ) {
                        if (response.isSuccessful) {
                            userValid.value = response!!.body()
                            Log.d("TestSuccess", response!!.body().toString())
                        } else {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            userValid.value = UserValid(error = jObjError["error"].toString(), token = "")
                        }
                    }

                    override fun onFailure(call: Call<UserValid>, t: Throwable?) {
                        Log.e("error", t?.message!!)
                        userValid.value = UserValid(error = t?.message!!, token = "")
                    }

                })
    }

    fun signup(user: User) {
        getApi()?.signup(user)
                ?.enqueue(object : Callback<UserValid> {
                    override fun onResponse(
                            call: Call<UserValid>?,
                            response: Response<UserValid>?
                    ) {
                        Log.d("TestSuccess", response!!.body().toString())
                    }

                    override fun onFailure(call: Call<UserValid>?, t: Throwable?) {
                        Log.e("error", t?.message!!)
                    }

                })
    }

}
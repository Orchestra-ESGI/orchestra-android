package core.rest.client

import android.util.Log
import androidx.lifecycle.MutableLiveData
import core.rest.model.ApiError
import core.rest.model.User
import core.rest.model.UserValid
import core.rest.services.RootApiService
import core.rest.services.UserServices
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object UserClient {
    private var userServices: UserServices? = getApi()
    var userValid: MutableLiveData<UserValid> = MutableLiveData()
    var apiError: MutableLiveData<ApiError> = MutableLiveData()

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
        userValid.value = null
        apiError.value = null

        getApi()?.login(user)
                ?.enqueue(object : Callback<UserValid> {
                    override fun onResponse(
                            call: Call<UserValid>,
                            response: Response<UserValid>
                    ) {
                        if (response.isSuccessful) {
                            apiError.value = null
                            userValid.value = response.body()
                        } else {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            apiError.value = ApiError(jObjError["error"].toString(), code = 403)

                        }
                    }
                    override fun onFailure(call: Call<UserValid>, t: Throwable?) {
                        Log.e("error", t?.message!!)
                        apiError.value = ApiError("", code = 404)
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
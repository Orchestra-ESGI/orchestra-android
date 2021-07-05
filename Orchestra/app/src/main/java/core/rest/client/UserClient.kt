package core.rest.client

import android.content.Context
import androidx.lifecycle.MutableLiveData
import core.rest.model.User
import core.rest.model.UserValid
import core.rest.services.RootApiService
import core.rest.services.UserServices
import core.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
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

    fun login(context: Context, user: User) {
        getApi()?.login(user)
                ?.enqueue(object : Callback<UserValid> {
                    override fun onResponse(
                            call: Call<UserValid>,
                            response: Response<UserValid>
                    ) {
                        if (response.isSuccessful) {
                            userValid.value = response.body()
                        } else {
                            ApiUtils.handleError(context, response.code())
                        }
                    }
                    override fun onFailure(call: Call<UserValid>, t: Throwable?) {
                        ApiUtils.handleError(context, 500)
                    }

                })
    }

    fun signup(context: Context, user: User) {
        getApi()?.signup(user)
                ?.enqueue(object : Callback<UserValid> {
                    override fun onResponse(
                            call: Call<UserValid>,
                            response: Response<UserValid>
                    ) {
                        if (!response.isSuccessful) {
                            ApiUtils.handleError(context, response.code())
                        }
                    }

                    override fun onFailure(call: Call<UserValid>?, t: Throwable?) {
                        ApiUtils.handleError(context, 500)
                    }

                })
    }

}
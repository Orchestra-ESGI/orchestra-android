package core.rest.client

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import core.rest.model.FirebaseToken
import core.rest.model.User
import core.rest.model.UserValid
import core.rest.services.RootApiService
import core.rest.services.UserServices
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object UserClient {
    private var userServices: UserServices? = getApi(context = null)
    var userValid: MutableLiveData<UserValid> = MutableLiveData()
    private var fcmToken: String = ""

    private fun getApi(context: Context?): UserServices? {
        val sharedPref = context?.getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE)
        val token = sharedPref?.getString("Token", "")
        if (token != null) {
            this.fcmToken = token
        }
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor { chain ->
            val request: Request = chain.request().newBuilder().
                addHeader("App-Key", "orchestra").
                addHeader("Authorization","Bearer ${this.fcmToken}"
            ).build()
            chain.proceed(request)
        }
        if (userServices == null) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(RootApiService.ROOT_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.build())
                    .build()
            userServices = retrofit.create(UserServices::class.java)
        }
        return userServices
    }

    fun login(context: Context, user: User) {
        getApi(context)?.login(user)
                ?.enqueue(object : Callback<UserValid> {
                    override fun onResponse(
                            call: Call<UserValid>,
                            response: Response<UserValid>
                    ) {
                        if (response.isSuccessful) {
                            userValid.value = response.body()
                        } else {
                            RootApiService.handleError(context, response.code())
                        }
                    }
                    override fun onFailure(call: Call<UserValid>, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

    fun sendFcmToken(context: Context, fcmToken: String) {
        val body : HashMap<String, String> = HashMap()
        body["token"] = fcmToken
        getApi(context)?.sendToken(body)?.enqueue(object : Callback<HashMap<String, Any>> {
            override fun onResponse(
                call: Call<HashMap<String, Any>>,
                response: Response<HashMap<String, Any>>
            ) {
                Log.d("response", response.toString())
            }

            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {
                RootApiService.handleError(context, 500)
            }
        })
    }
/*
    fun sendFcmToken(context: Context, fcmToken: String) {

        getApi(context)?.sendFcmToken(FirebaseToken(fcmToken))?.enqueue(object : Callback<HashMap<String, Any>> {
            override fun onResponse(call: Call<HashMap<String, Any>>, response: Response<HashMap<String, Any>>) {

            }

            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {

            }
        })
    }
*/
    fun signup(context: Context, user: User) {
        getApi(context)?.signup(user)
                ?.enqueue(object : Callback<UserValid> {
                    override fun onResponse(
                            call: Call<UserValid>,
                            response: Response<UserValid>
                    ) {
                        if (!response.isSuccessful) {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            userValid.value = UserValid(token = "", error = jObjError["error"].toString())
                        } else {
                            userValid.value = UserValid(token = "", error = "")
                        }
                    }

                    override fun onFailure(call: Call<UserValid>?, t: Throwable?) {
                        RootApiService.handleError(context, 500)
                    }

                })
    }

}
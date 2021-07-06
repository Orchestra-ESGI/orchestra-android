package core.rest.services

import core.rest.model.FirebaseToken
import core.rest.model.User
import core.rest.model.UserValid
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface UserServices {

    @POST("user/login")
    fun login(@Body user: User) : Call<UserValid>

    @POST("user/signup")
    fun signup(@Body user: User) : Call<UserValid>

    @POST("/notification")
    fun sendFcmToken(@Body fcmToken: FirebaseToken): Call<HashMap<String, Any>>

    @POST("/notification")
    fun sendToken(@Body body: HashMap<String, String>): Call<HashMap<String, Any>>
}
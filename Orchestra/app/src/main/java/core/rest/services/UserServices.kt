package core.rest.services

import core.rest.model.User
import core.rest.model.UserValid
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface UserServices {

    @POST("user/login")
    fun login(@Body user: User) : Call<UserValid>

    @POST("user/signup")
    fun signup(@Body user: User) : Call<UserValid>
}
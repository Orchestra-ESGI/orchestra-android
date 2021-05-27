package core.rest.services

import core.rest.model.User
import io.reactivex.Observable
import retrofit2.http.*

interface UserServices {
    @GET("/users/get/all")
    fun getAllUsers(): Observable<List<User>>

    @DELETE("/users/get/all")
    fun removeUser(@Body userId : List<String>): Observable<List<User>>

    @POST("/users/account/login")
    fun login(@Body user: User) : Observable<User>

    @POST("/users/account/singin")
    fun signin(@Body user: User) : Observable<User>

    @PUT("/users/update/{credentialName}")
    fun updateUser(@Path("credentialName") credentialName: String, userId: String, credentialValue: String) : Observable<User>
}
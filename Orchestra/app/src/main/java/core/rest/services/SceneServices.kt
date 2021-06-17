package core.rest.services

import core.rest.model.Scene
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface SceneServices {
    @GET("/scene/all")
    fun getAllScenes() : Call<List<Scene>>

    @POST("/scene")
    fun addScene(@Body idScene : List<String>) : Call<List<Scene>>
}
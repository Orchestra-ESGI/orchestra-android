package core.rest.services

import core.rest.model.Scene
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET

interface SceneServices {
    @GET("/scenes/get/all")
    fun getAllScenes(@Body idHouse : String) : Observable<List<Scene>>

    @DELETE("/scenes/remove/group")
    fun removeScene(@Body idScene : List<String>) : Observable<List<Scene>>
}
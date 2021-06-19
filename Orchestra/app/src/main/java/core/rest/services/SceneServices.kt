package core.rest.services

import core.rest.model.ListScene
import core.rest.model.ListSceneToDelete
import core.rest.model.Scene
import retrofit2.Call
import retrofit2.http.*


interface SceneServices {
    @GET("/scene/all")
    fun getAllScenes() : Call<ListScene>

    @POST("/scene")
    fun addScene(@Body scene: Scene) : Call<Scene>

    @HTTP(method = "DELETE", path = "/scene", hasBody = true)
    fun deleteScenes(@Body scene: ListSceneToDelete) : Call<ListSceneToDelete>


}
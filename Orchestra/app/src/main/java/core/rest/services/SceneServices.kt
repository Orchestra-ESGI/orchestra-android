package core.rest.services

import core.rest.model.Automation
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

    @POST("/scene/{id}")
    fun launchScene(@Path("id") sceneId : String) : Call<Scene>

    @HTTP(method = "DELETE", path = "/scene", hasBody = true)
    fun deleteScenes(@Body scene: ListSceneToDelete) : Call<ListSceneToDelete>

    @PATCH("scene")
    fun updateScene(@Body scene: Scene) : Call<Scene>

    @POST("/automation")
    fun addAutomation(@Body automation: Automation) : Call<HashMap<String, Any>>
}
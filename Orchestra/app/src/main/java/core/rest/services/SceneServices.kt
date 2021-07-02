package core.rest.services

import core.rest.model.*
import retrofit2.Call
import retrofit2.http.*


interface SceneServices {
    @GET("/scene/all")
    fun getAllScenes() : Call<ListScene>

    @GET("/automation/all")
    fun getAllAutomation() : Call<ListAutomation>

    @POST("/scene")
    fun addScene(@Body scene: Scene) : Call<Scene>

    @POST("/scene/{id}")
    fun launchScene(@Path("id") sceneId : String) : Call<Scene>

    @HTTP(method = "DELETE", path = "/scene", hasBody = true)
    fun deleteScenes(@Body scene: ListSceneToDelete) : Call<ListSceneToDelete>

    @PATCH("scene")
    fun updateScene(@Body scene: Scene) : Call<Scene>

    @PATCH("automation")
    fun updateAutomation(@Body automation: Automation) : Call<HashMap<String, Any>>

    @POST("/automation")
    fun addAutomation(@Body automation: Automation) : Call<HashMap<String, Any>>

    @HTTP(method = "DELETE", path = "/automation", hasBody = true)
    fun deleteScenes(@Body idList: HashMap<String, List<String>>) : Call<HashMap<String, Any>>
}
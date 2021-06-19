package core.rest.model

import java.io.Serializable

class Scene (
        val _id: String = "",
        val name: String = "",
        val description: String = "",
        val color: String?,
        val devices: List<ActionsToSet>
) : Serializable

data class ListScene (
        val scenes : List<Scene>
        ) : Serializable

data class SceneActionsName(
        val type : String,
        val key : String,
        val value : String
)

data class ListSceneToDelete(
        val ids : List<String>
) : Serializable
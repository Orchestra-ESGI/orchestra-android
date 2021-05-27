package core.rest.model

import java.io.Serializable

class Scene (
        val id: String = "",
        val title: String = "",
        val sceneDescription: String = "",
        val backgroundColor: String?,
        val idUser: String = "",
        val actions: List<ActionScene>
) : Serializable
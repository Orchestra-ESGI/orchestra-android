package core.rest.model

import java.io.Serializable

class House (
        val id : String = "",
        val houseName : String = "",
        val houseAdress: String = "",
        val scenes: List<Scene>
        ) : Serializable
package core.rest.model
import java.io.Serializable

class User(
        val id: String = "",
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val is_removed: Boolean = false,
        val houses: List<House>
) : Serializable
package core.rest.model
import java.io.Serializable

class User(
        val email: String = "",
        val password: String = ""
) : Serializable

class UserValid(
        val token: String,
        var error: String
)
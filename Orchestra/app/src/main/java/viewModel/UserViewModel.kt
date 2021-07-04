package viewModel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.client.UserClient
import core.rest.model.ApiError
import core.rest.model.User
import core.rest.model.UserValid

class UserViewModel: ViewModel() {
    lateinit var context : AppCompatActivity
    var userService : UserClient = UserClient
    var userValid: MutableLiveData<UserValid> = MutableLiveData()
    var apiError: MutableLiveData<ApiError> = MutableLiveData()

    fun signup(user : User) {
        userService.signup(user)
    }

    fun login(user : User) {
        userService.userValid.observe(context, Observer {
            userValid.value = it
        })
        userService.apiError.observe(context, Observer {
            apiError.value = it
        })
        userService.login(user)
    }

}
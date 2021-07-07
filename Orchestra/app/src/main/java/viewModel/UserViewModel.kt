package viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import core.rest.client.UserClient
import core.rest.model.User
import core.rest.model.UserValid
import core.rest.services.UserServices
import core.utils.SingleLiveEvent
import okhttp3.RequestBody

class UserViewModel: ViewModel() {
    lateinit var context : AppCompatActivity
    var userService : UserClient = UserClient
    var userValid: SingleLiveEvent<UserValid> = SingleLiveEvent()

    fun signup(user : User) {
        userService.userValid.observe(context, Observer {
            userValid.value = it
        })
        userService.signup(context, user)
    }

    fun login(user : User) {
        userService.userValid.observe(context, Observer {
            userValid.value = it
        })
        userService.login(context, user)
    }


    fun sendFcmToken(fcmToken: String) {
        userService.sendFcmToken(context, fcmToken)
    }

    fun deleteAccount(email: String) {
        userService.deleteAccount(context, email)
    }

    fun resetFactory() {
        userService.resetFactory()
    }

    fun shutdown() {
        userService.shutdown()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
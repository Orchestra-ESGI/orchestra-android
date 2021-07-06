package core.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.orchestra.BuildConfig
import com.example.orchestra.R
import view.ui.LoginActivity

object ApiUtils {
    // var API_URL = "http://orchestra.local:3000/"
    // var API_URL = "http://192.168.1.33:3000/" // Nass
    // var API_URL = "http://192.168.1.130:3000/" // Ramzy
    // var API_URL = "http://192.168.1.105:3000/"
    // var API_URL = "http://10.33.1.219:3000/" // Ecole
    // var API_URL = "http://10.33.1.167:3000/" // local
    // var API_URL = "http://10.0.2.2:3000/"
    var API_URL = "http://10.33.1.219:3000/"

    fun getVersion() : String {
        return BuildConfig.VERSION_NAME
    }

    fun getDomain() : String {
        return API_URL
    }
}
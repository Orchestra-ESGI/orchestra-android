package core.utils

import com.example.orchestra.BuildConfig

object ApiUtils {
    // var API_URL = "http://orchestra.local:3000/"
    var API_URL = "http://192.168.1.33:3000/" // Nass
    // var API_URL = "http://192.168.1.130:3000/" // Ramzy
    // var API_URL = "http://192.168.1.105:3000/"
    // var API_URL = "http://10.33.1.219:3000/" // Ecole
    var API_SECRET = "TOKEN"
    var API_TOKEN = ""

    fun getVersion() : String {
        return BuildConfig.VERSION_NAME
    }

    fun getDomain() : String {
        return API_URL
    }

    fun getClientSecret() : String {
        return API_SECRET
    }
}
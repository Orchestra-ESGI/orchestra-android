package core.utils

import com.example.orchestra.BuildConfig

object ApiUtils {
    var API_URL = "http://localhost:3000"
    var API_SECRET = "TOKEN"

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
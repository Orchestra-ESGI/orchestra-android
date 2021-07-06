package view.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.orchestra.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val sharedPref = getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE)
        val token = sharedPref.getString("Token", "")

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent()
            if (token == "" || token == null) {
                intent.setClass(this, LoginActivity::class.java)
            } else {
                intent.setClass(this, HomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            if (token != null) {
                getSharedPreferences("com.example.orchestra.API_TOKEN", MODE_PRIVATE)
                    .edit()
                    .putString("fcmToken", token)
                    .apply()
                Log.d("FCM Token", token)
            }
        })
    }
}
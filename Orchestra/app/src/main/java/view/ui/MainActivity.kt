package view.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.example.orchestra.R

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
    }
}
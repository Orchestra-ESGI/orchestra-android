package view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.orchestra.R

class LoginActivity : AppCompatActivity() {

    private var createAccountTv: TextView? = null
    private var connexionBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        createAccountTv = findViewById(R.id.login_create_account_tv)
        connexionBtn = findViewById(R.id.login_connect_btn)

        createAccountTv?.setOnClickListener {
            val createAccountIntent =  Intent(this, SignInActivity::class.java)
            startActivity(createAccountIntent)
        }

        connexionBtn?.setOnClickListener {
            val sceneListIntent =  Intent(this, HomeActivity::class.java)
            startActivity(sceneListIntent)
        }
    }
}
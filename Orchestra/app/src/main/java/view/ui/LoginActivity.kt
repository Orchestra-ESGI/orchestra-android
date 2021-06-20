package view.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.orchestra.R
import core.rest.model.User
import viewModel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var createAccountTv: TextView
    private lateinit var connexionBtn: Button
    private lateinit var userVM: UserViewModel

    private lateinit var Prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        createAccountTv = findViewById(R.id.login_create_account_tv)
        connexionBtn = findViewById(R.id.login_connect_btn)
        emailEditText = findViewById(R.id.login_id_et)
        passwordEditText = findViewById(R.id.login_pwd_et)
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userVM.context = this


        val sharedPref = getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        createAccountTv.setOnClickListener {
            val createAccountIntent =  Intent(this, SignInActivity::class.java)
            startActivity(createAccountIntent)
        }

        connexionBtn.setOnClickListener {
            if(checkFields()) {
                val user = retrieveData()
                userVM.userValid.observe(this, Observer {
                    if (it != null) {
                        if (it.token != "") {
                            with (sharedPref.edit()) {
                                putString("Token", it.token)
                                apply()
                            }
                            val sceneListIntent = Intent(this, HomeActivity::class.java)
                            startActivity(sceneListIntent)
                        } else {
                            Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                        }
                    }
                })
                userVM.login(user)
            }
        }
    }

    private fun checkFields() : Boolean {
        // Vérifier si email correspond au bon format
        // Vérifier pwd >= 5
        return true
    }

    private fun retrieveData() : User {
        return User(email = emailEditText.text.toString(), password = passwordEditText.text.toString())
    }
}
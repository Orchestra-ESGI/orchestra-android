package view.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.orchestra.R
import core.rest.model.User
import viewModel.HomeViewModel
import viewModel.UserViewModel


class SignInActivity : AppCompatActivity() {

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var userVM : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bind()
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
    }

    fun bind() {
        emailEditText = findViewById(R.id.create_account_email_et)
        passwordEditText = findViewById(R.id.create_account_pwd_et)
        confirmPasswordEditText = findViewById(R.id.create_account_confirm_pwd_et)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_account_menu, menu)
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_account_continue_btn -> {
            if(checkFields()) {
                val user = retrieveData()
                userVM.signup(user)
                onBackPressed()
            } else {
                AlertDialog.Builder(this)
                        .setTitle("Mail envoyé")
                        .setMessage("Un mail de confirmation vous a été envoyé.\n Revenez sur Orchestra après avoir validé le mail")
                        .show()
            }

            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun checkFields() : Boolean {
        // Vérifier les champs
        // Email : Si l'entrée correspond bien à un format email
        // PWD : vérifier si les pwd correspondent bien
        return true
    }

    private fun retrieveData() : User {
        return User(email = emailEditText.text.toString(), password = passwordEditText.text.toString())
    }
}
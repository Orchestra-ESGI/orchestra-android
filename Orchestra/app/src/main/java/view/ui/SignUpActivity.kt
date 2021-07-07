package view.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.orchestra.R
import core.rest.model.User
import viewModel.UserViewModel


class SignUpActivity : AppCompatActivity() {

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var userVM : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.login_background))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        bind()
        setFilterOnEditText()
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userVM.context = this
        setObservers()
        setListenerOnButton()
    }

    fun bind() {
        emailEditText = findViewById(R.id.create_account_email_et)
        passwordEditText = findViewById(R.id.create_account_pwd_et)
        confirmPasswordEditText = findViewById(R.id.create_account_confirm_pwd_et)
        continueButton = findViewById(R.id.create_account_continue_btn)
    }

    private fun setFilterOnEditText() {
        val filter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (Character.isWhitespace(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }
        emailEditText.filters = arrayOf(filter)
    }

    private fun setObservers() {
        userVM.userValid.observe(this, Observer {
            if(it.error != "") {
                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            } else {
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.sigup_mail_send))
                        .setMessage(getString(R.string.sigup_mail_send_message))
                        .setPositiveButton(R.string.detail_device_ok) { dialog, which ->
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        .show()
            }
        })
    }

    private fun setListenerOnButton() {
        continueButton.setOnClickListener {
            if(checkFields()) {
                val user = retrieveData()
                userVM.signup(user)
            }
        }
    }

    private fun checkFields() : Boolean {
        if (emailEditText.text.toString() == "" || passwordEditText.text.toString() == "" || confirmPasswordEditText.text.toString() == "") {
            Toast.makeText(this, getString(R.string.sigup_fields_empty), Toast.LENGTH_LONG).show()
            return false
        }
        if (passwordEditText.text.toString() != confirmPasswordEditText.text.toString()) {
            Toast.makeText(this, getString(R.string.sigup_password_wrong), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun retrieveData() : User {
        return User(email = emailEditText.text.toString(), password = passwordEditText.text.toString())
    }
}
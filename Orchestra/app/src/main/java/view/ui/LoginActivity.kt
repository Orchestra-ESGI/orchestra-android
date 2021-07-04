package view.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.orchestra.R
import core.rest.model.User
import viewModel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var createAccountTv: TextView
    private lateinit var connexionBtn: Button
    private lateinit var userVM: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.login_background))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bind()
        setStyle()
        setFilterEditText()

        val sharedPref = getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE)

        createAccountTv.setOnClickListener {
            val createAccountIntent =  Intent(this, SignInActivity::class.java)
            startActivity(createAccountIntent)
        }

        connexionBtn.setOnClickListener {
            if(checkFields()) {
                val user = retrieveData()
                userVM.apiError.observe(this, Observer {
                    if( it != null) {
                        if (it.code == 404) {
                            Toast.makeText(this, getString(R.string.error_404), Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, getString(R.string.error_user), Toast.LENGTH_LONG).show()
                        }
                    }
                })
                userVM.userValid.observe(this, Observer {
                    if (it != null) {
                        if (it.token != "") {
                            with (sharedPref.edit()) {
                                putString("Token", it.token)
                                putString("Email", emailEditText.text.toString())
                                apply()
                            }
                            val sceneListIntent = Intent(this, HomeActivity::class.java)
                            startActivity(sceneListIntent)
                        } else {
                            Toast.makeText(this, getString(R.string.error_user), Toast.LENGTH_LONG).show()
                        }
                    }
                })
                userVM.login(user)
            }
        }
    }

    private fun bind() {
        titleTextView = findViewById(R.id.login_title_tv)
        createAccountTv = findViewById(R.id.login_create_account_tv)
        connexionBtn = findViewById(R.id.login_connect_btn)
        emailEditText = findViewById(R.id.login_id_et)
        passwordEditText = findViewById(R.id.login_pwd_et)
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userVM.context = this
    }

    private fun setStyle() {

        val titleText = getText(R.string.title) as SpannedString
        val annotations = titleText.getSpans(0, titleText.length, android.text.Annotation::class.java)
        val spannableString = SpannableString(titleText)

        for (annotation in annotations) {
            // look for the span with the key "font"
            if (annotation.key == "font") {
                val fontName = annotation.value
                // check the value associated with the annotation key
                if (fontName == "title_login_welcome") {
                    // create the typeface
                    val typeface = ResourcesCompat.getFont(this, R.font.gilroy_extra_bold)
                    // set the span to the same indices as the annotation
                    spannableString.setSpan(typeface,
                            titleText.getSpanStart(annotation),
                            titleText.getSpanEnd(annotation),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }

        titleTextView.text = spannableString
    }

    private fun setFilterEditText() {
        val filter = InputFilter { source, start, end, dest, dstart, dend ->

            for (i in start until end) {
                if (Character.isWhitespace(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }

        emailEditText.filters = arrayOf(filter)
    }

    private fun checkFields() : Boolean {
        // Vérifier si email correspond au bon format
        // Vérifier pwd >= 5
        return true
    }

    private fun retrieveData() : User {
        return User(email = emailEditText.text.toString(), password = passwordEditText.text.toString())
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }
}
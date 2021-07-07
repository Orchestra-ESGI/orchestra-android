package view.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.orchestra.R
import core.rest.model.User
import view.Home.HomeActivity
import viewModel.UserViewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var faqImageView : ImageView
    private lateinit var titleTextView: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var createAccountTv: TextView
    private lateinit var connexionBtn: Button
    private lateinit var userVM: UserViewModel

    private var inputTypePassword : Int = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.login_background))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bind()
        setStyle()
        setFilterEditText()

        val sharedPref = getSharedPreferences(
            "com.example.orchestra.API_TOKEN",
            Context.MODE_PRIVATE
        )
        sharedPref
            .edit()
            .remove("Email")
            .remove("Token")
            .apply()

        createAccountTv.setOnClickListener {
            val createAccountIntent =  Intent(this, SignUpActivity::class.java)
            startActivity(createAccountIntent)
        }

        connexionBtn.setOnClickListener {
            if(checkFields()) {
                val user = retrieveData()
                userVM.userValid.observe(this, Observer {
                    if (it != null) {
                        if (it.token != "") {
                            sharedPref.edit()
                                .putString("Token", it.token)
                                .putString("Email", emailEditText.text.toString())
                                .apply()
                            val sceneListIntent = Intent(this, HomeActivity::class.java)
                            val fcmToken = sharedPref.getString("fcmToken", "")
                            if (fcmToken != null) {
                                userVM.sendFcmToken(fcmToken)
                            }
                            startActivity(sceneListIntent)
                        } else {
                            Toast.makeText(this, getString(R.string.error_user), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
                userVM.login(user)
            }
        }

        faqImageView.setOnClickListener {
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
            arrayAdapter.add(getString(R.string.login_faq))
            arrayAdapter.add(getString(R.string.login_shutdown))
            arrayAdapter.add(getString(R.string.login_reset_factory))

            AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.login_faq_alert_title))
                .setAdapter(arrayAdapter) { _, which ->
                    when(which) {
                        0 -> {
                            val intent = Intent(this, WebViewActivity::class.java)
                            intent.putExtra("URL", "https://orchestra-website.herokuapp.com/faq")
                            startActivity(intent)
                        }
                        1 -> userVM.shutdown()
                        2 -> userVM.resetFactory()
                        else -> {}
                    }
                }
                .setNegativeButton(getString(R.string.login_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
        }

        passwordEditText.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_RIGHT = 2;

                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (passwordEditText.right - passwordEditText.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        if (passwordEditText.inputType == inputTypePassword) passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        else passwordEditText.inputType = inputTypePassword
                        return true
                    }
                }
                return false
            }

        })
    }

    private fun bind() {
        faqImageView = findViewById(R.id.login_faq_iv)
        titleTextView = findViewById(R.id.login_title_tv)
        createAccountTv = findViewById(R.id.login_create_account_tv)
        connexionBtn = findViewById(R.id.login_connect_btn)
        emailEditText = findViewById(R.id.login_email_et)
        passwordEditText = findViewById(R.id.login_pwd_et)
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userVM.context = this

        inputTypePassword = passwordEditText.inputType
    }

    private fun setStyle() {

        val titleText = getText(R.string.title) as SpannedString
        val annotations = titleText.getSpans(
            0,
            titleText.length,
            android.text.Annotation::class.java
        )
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
                    spannableString.setSpan(
                        typeface,
                        titleText.getSpanStart(annotation),
                        titleText.getSpanEnd(annotation),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        titleTextView.text = spannableString
    }

    private fun setFilterEditText() {
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

    private fun checkFields() : Boolean {
        // Vérifier si email correspond au bon format
        // Vérifier pwd >= 5
        return true
    }

    private fun retrieveData() : User {
        return User(
            email = emailEditText.text.toString(),
            password = passwordEditText.text.toString()
        )
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }
}
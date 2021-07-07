package view.Settings

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import com.kaopiz.kprogresshud.KProgressHUD
import utils.OnSettingListener
import viewModel.UserViewModel


class SettingsActivity : AppCompatActivity(), OnSettingListener {

    private lateinit var settingsProfileImageView: ImageView
    private lateinit var settingsProfileTextView: TextView
    private lateinit var settingsRecyclerView: RecyclerView
    private lateinit var settingsAdapter: SettingsAdapter
    private lateinit var userViewModel: UserViewModel

    private lateinit var progressBar: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        title = getString(R.string.settings_title)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.context = this

        settingsProfileImageView = findViewById(R.id.settings_profile_iv)
        settingsProfileTextView = findViewById(R.id.settings_profile_tv)
        settingsRecyclerView = findViewById(R.id.settings_rv)

        settingsProfileTextView.text = getSharedPreferences(
            "com.example.orchestra.API_TOKEN",
            Context.MODE_PRIVATE
        ).getString("Email", "")

        settingsRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        settingsAdapter = SettingsAdapter(this, this)
        settingsRecyclerView.adapter = settingsAdapter

        progressBar = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
            .setLabel(getString(R.string.settings_please_wait))
            .setMaxProgress(100)
    }

    override fun onDeleteAccount() {
        val email = getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE).getString(
            "Email",
            ""
        )
        if (email != null) {
            userViewModel.deleteAccount(email)
        }
    }

    override fun onRebootHub() {
        userViewModel.resetFactory()
        progressBar.setProgress(90)
        progressBar.show()

    }

    override fun onShutdownHub() {
        // userViewModel.shutdown()
        progressBar.setProgress(90)
        progressBar.show()
    }
}
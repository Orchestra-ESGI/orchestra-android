package view.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import view.adapter.SettingsAdapter

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsProfileImageView: ImageView
    private lateinit var settingsProfileTextView: TextView
    private lateinit var settingsRecyclerView: RecyclerView
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPref = getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE)

        title = getString(R.string.settings_title)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()

        settingsProfileImageView = findViewById(R.id.settings_profile_iv)
        settingsProfileTextView = findViewById(R.id.settings_profile_tv)
        settingsRecyclerView = findViewById(R.id.settings_rv)

        settingsProfileTextView.text = sharedPref.getString("Email", "")

        settingsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        settingsAdapter = SettingsAdapter(this)
        settingsRecyclerView.adapter = settingsAdapter
    }
}
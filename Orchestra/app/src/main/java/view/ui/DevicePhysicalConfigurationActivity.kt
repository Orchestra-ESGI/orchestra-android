package view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.orchestra.R
import core.rest.model.SupportedDeviceInformations

class DevicePhysicalConfigurationActivity : AppCompatActivity() {

    private lateinit var devicePhysicalConfigurationUrl : TextView

    var supportedDevice : SupportedDeviceInformations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_physical_configuration)
        devicePhysicalConfigurationUrl = findViewById(R.id.device_physical_configuration_step_one_url)

        title = getString(R.string.device_physical_configuration_tutoriel)

        supportedDevice = intent.getSerializableExtra("SupportedDevice") as? SupportedDeviceInformations

        if (supportedDevice != null) {
            devicePhysicalConfigurationUrl.text = supportedDevice!!.documentation

            devicePhysicalConfigurationUrl.setOnClickListener {
                val webViewIntent = Intent(this, WebViewActivity::class.java)
                webViewIntent.putExtra("URL", supportedDevice!!.documentation)
                startActivity(webViewIntent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_account_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_account_continue_btn -> {
            val intent = Intent(applicationContext, SearchDeviceActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
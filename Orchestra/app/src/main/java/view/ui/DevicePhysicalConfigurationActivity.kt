package view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.orchestra.R
import core.rest.model.hubConfiguration.HubAccessoryConfiguration

class DevicePhysicalConfigurationActivity : AppCompatActivity() {

    var hubAccessoryConfiguration : HubAccessoryConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_physical_configuration)

        hubAccessoryConfiguration = intent.getSerializableExtra("CreatedScene") as? HubAccessoryConfiguration
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_account_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_account_continue_btn -> {
            val intent = Intent(applicationContext, SearchDeviceActivity::class.java)
            intent.putExtra("CreatedScene", hubAccessoryConfiguration)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
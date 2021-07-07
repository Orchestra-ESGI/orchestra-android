package view.SupportedDevice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.device.Device
import view.SupportedDevice.adapter.SupportedAccessoriesAdapter
import viewModel.DeviceViewModel

class SupportedAccessoriesListActivity : AppCompatActivity() {
    private lateinit var deviceViewModel : DeviceViewModel

    private lateinit var supportedDeviceTypeListRecyclerView: RecyclerView
    private lateinit var supportedAccessoriesAdapter : SupportedAccessoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supported_accessories_list)

        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceViewModel.context = this

        deviceViewModel.supportedAccessorieList.observe(this, androidx.lifecycle.Observer {
            supportedAccessoriesAdapter.supportedDevicesList = it
        })
        deviceViewModel.getSupportedAccessories()

        title = getString(R.string.supported_accessories_title)

        supportedDeviceTypeListRecyclerView = findViewById(R.id.supported_device_type_rv)
        supportedDeviceTypeListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        supportedAccessoriesAdapter = SupportedAccessoriesAdapter()
        supportedAccessoriesAdapter.device = intent.getSerializableExtra("device") as? Device
        supportedDeviceTypeListRecyclerView.adapter = supportedAccessoriesAdapter
    }
}
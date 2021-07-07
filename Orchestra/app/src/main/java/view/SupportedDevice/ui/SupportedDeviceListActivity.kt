package view.SupportedDevice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.SupportedDeviceInformations
import core.rest.model.device.Device
import view.SupportedDevice.adapter.SupportedDeviceAdapter
import viewModel.DeviceViewModel

class SupportedDeviceListActivity : AppCompatActivity() {

    private lateinit var supportedDeviceListRecyclerView: RecyclerView
    private lateinit var supportedDeviceAdapter : SupportedDeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supported_device_list)

        val data : List<SupportedDeviceInformations>? = intent.getSerializableExtra("SupportedDevice") as? List<SupportedDeviceInformations>

        var deviceVM = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceVM.context = this

        val brand = intent.getStringExtra("brand")
        val device = intent.getSerializableExtra("device") as? Device

        supportedDeviceListRecyclerView = findViewById(R.id.supported_device_rv)
        supportedDeviceListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        supportedDeviceAdapter = SupportedDeviceAdapter()
        supportedDeviceAdapter.device = device
        supportedDeviceAdapter.brand = brand
        supportedDeviceAdapter.supportedDeviceList = data!!
        supportedDeviceAdapter.deviceVM = deviceVM
        supportedDeviceListRecyclerView.adapter = supportedDeviceAdapter

        if (brand != null) {
            title = brand
        }
    }
}
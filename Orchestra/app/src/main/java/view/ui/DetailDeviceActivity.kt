package view.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.Device
import view.adapter.DetailDeviceSpecificationAdapter
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DetailDeviceActivity : AppCompatActivity() {

    private lateinit var detailDeviceKeyValue: HashMap<String, String>
    private lateinit var detailDeviceSpecificationInfoNames: ArrayList<String>
    private lateinit var detailDeviceAdapter : DetailDeviceSpecificationAdapter

    private lateinit var detailDeviceName : TextView
    private lateinit var detailDeviceRoom : TextView

    private lateinit var deviceDetailRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_device)

        detailDeviceSpecificationInfoNames = ArrayList()
        detailDeviceKeyValue = HashMap()
        detailDeviceAdapter = DetailDeviceSpecificationAdapter()

        detailDeviceName = findViewById(R.id.detail_device_title_tv)
        detailDeviceRoom = findViewById(R.id.detail_device_room_tv)
        deviceDetailRecyclerView = findViewById(R.id.detail_device_specifications_lv)
        deviceDetailRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(
            deviceDetailRecyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        deviceDetailRecyclerView.addItemDecoration(dividerItemDecoration)

        val deviceDetail = intent.getSerializableExtra("DetailDevice") as? Device

        detailDeviceName.text = deviceDetail!!.name
        detailDeviceName.setTextColor(Color.parseColor(deviceDetail.backgroundColor))
        detailDeviceRoom.text = deviceDetail.roomName

        val manufacturerLabel = getString(R.string.specification_manufacturer)
        val serialNumberLabel = getString(R.string.specification_serial_number)
        val modelLabel = getString(R.string.specification_model)
        val versionLabel = getString(R.string.specification_version)
        val reachabilityLabel = getString(R.string.specification_reachable)

        val reachabilityStatus =  if (deviceDetail.isReachable == true) "Disponibile" else  "Non Disponible"

        detailDeviceSpecificationInfoNames.add(manufacturerLabel)
        detailDeviceSpecificationInfoNames.add(serialNumberLabel)
        detailDeviceSpecificationInfoNames.add(modelLabel)
        detailDeviceSpecificationInfoNames.add(versionLabel)
        detailDeviceSpecificationInfoNames.add(reachabilityLabel)

        detailDeviceKeyValue[manufacturerLabel] = deviceDetail.manufacturer!!
        detailDeviceKeyValue[serialNumberLabel] = deviceDetail.serialNumber!!
        detailDeviceKeyValue[modelLabel] = deviceDetail.model!!
        detailDeviceKeyValue[versionLabel] = deviceDetail.version!!
        detailDeviceKeyValue[reachabilityLabel] = reachabilityStatus

        detailDeviceAdapter.deviceSpecificationInfoNames = detailDeviceSpecificationInfoNames
        detailDeviceAdapter.deviceSpecificationList = detailDeviceKeyValue

        deviceDetailRecyclerView.adapter = detailDeviceAdapter

    }
}
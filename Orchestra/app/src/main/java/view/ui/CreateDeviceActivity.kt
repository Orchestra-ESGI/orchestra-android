package view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.Actions
import core.rest.model.Room
import core.rest.model.SupportedDeviceInformations
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.HubAccessoryType
import utils.OnItemClicked
import view.adapter.ShuffleColorAdapter
import viewModel.DeviceViewModel
import kotlin.random.Random


class CreateDeviceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var nameTitleTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var whichRoomTitleTextView: TextView
    private lateinit var roomSpinner: Spinner

    private var device : HubAccessoryConfiguration? = null
    private var supportedDevice : SupportedDeviceInformations? = null
    private var brand : String? = null
    private var roomList : List<Room> = ArrayList()
    private var selectedPosition = -1

    private lateinit var deviceViewModel : DeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_device)

        bind()
        getIntentInfos()
        init()
        setUpObserver()
        deviceViewModel.getAllRoom()
    }

    private fun getIntentInfos() {
        device = intent.getSerializableExtra("device") as? HubAccessoryConfiguration
        supportedDevice = intent.getSerializableExtra("SupportedDevice") as? SupportedDeviceInformations
        brand = intent.getStringExtra("brand")

        nameEditText.setText(device?.name)
    }

    private fun bind() {
        nameTitleTextView = findViewById(R.id.create_device_name_tv)
        nameEditText = findViewById(R.id.create_device_name_et)
        whichRoomTitleTextView = findViewById(R.id.create_device_which_room_tv)
        roomSpinner = findViewById(R.id.create_device_room_spinner)
    }

    private fun init() {
        title = if(device != null) {
            getString(R.string.create_device_modify_device)
        } else {
            getString(R.string.create_device_new_device)
        }
    }

    private fun setUpObserver() {
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceViewModel.context = this

        deviceViewModel.roomList.observe(this, Observer {
            roomList = it
            setUpRoomSpinner()
        })
    }

    private fun setUpRoomSpinner() {
        roomSpinner.onItemSelectedListener = this
        val arrayAdapter = ArrayAdapter<String>(this, R.layout.spinner_room_item)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        roomList.forEach {
            arrayAdapter.add(it.name)
        }

        roomSpinner.adapter = arrayAdapter
        val deviceRoom = roomList.first { room -> room._id == device?.room?._id }
        val index = roomList.indexOf(deviceRoom)
        roomSpinner.setSelection(index)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_device_menu, menu)
        return true
    }

    private fun retrieveData() : HubAccessoryConfiguration {
        val name = nameEditText.text.toString()
        val type : HubAccessoryType?

        val room = if (selectedPosition == -1) device!!.room else roomList[selectedPosition]

        type = when (supportedDevice?.type) {
            "lightbulb" -> HubAccessoryType.lightbulb
            "switch" -> HubAccessoryType.switch
            "sensor" -> HubAccessoryType.sensor
            "contact" -> HubAccessoryType.contact
            "programmableswitch" -> HubAccessoryType.programmableswitch
            "temperatureandhumidity" -> HubAccessoryType.temperatureandhumidity
            "occupancy" -> HubAccessoryType.occupancy
            else -> { // Note the block
                device?.type
            }
        }

        val id = if(device == null) null else device!!._id
        val friendlyName = if(device == null) null else device!!.friendly_name

        return HubAccessoryConfiguration(id, name, room, null, brand, supportedDevice?.model, null, null, type, Actions(null, null, null, null), friendlyName)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_scene_add_btn -> {
            val intent = Intent()
            val device = retrieveData()
            intent.putExtra("CreatedScene", retrieveData())
            if (supportedDevice == null) {
                deviceViewModel.saveDevice(device)
                onBackPressed()
            } else {
                if (supportedDevice?.documentation != null) {
                    intent.setClass(applicationContext, DevicePhysicalConfigurationActivity::class.java)
                    startActivity(intent)
                } else {
                    intent.setClass(applicationContext, SearchDeviceActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedPosition = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}
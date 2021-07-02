package view.ui

import android.annotation.SuppressLint
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import com.kaopiz.kprogresshud.KProgressHUD
import core.rest.model.*
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.HubAccessoryType
import core.utils.ColorPicker
import view.adapter.DetailDeviceSpecificationAdapter
import viewModel.DeviceViewModel
import java.io.Serializable


class DetailDeviceActivity : AppCompatActivity() {

    private lateinit var detailDeviceKeyValue: HashMap<String, String>
    private lateinit var detailDeviceSpecificationInfoNames: ArrayList<String>
    private lateinit var detailDeviceAdapter : DetailDeviceSpecificationAdapter

    private lateinit var detailDeviceName : TextView
    private lateinit var detailDeviceRoom : TextView
    private lateinit var deviceDetailRecyclerView: RecyclerView

    private lateinit var configLinearLayout: LinearLayout
    private lateinit var configurateButton : Button

    private lateinit var stateLayout : LinearLayout
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var stateSwitch : Switch

    private lateinit var brightnessLayout : LinearLayout
    private lateinit var brightnessSlider : SeekBar

    private lateinit var colorLayout : LinearLayout
    private lateinit var colorSlider : ColorPicker

    private lateinit var colorTemperatureLayout : LinearLayout
    private lateinit var colorTemperatureSlider : SeekBar

    private lateinit var deviceVM : DeviceViewModel

    private var deviceDetail : HubAccessoryConfiguration? = null
    private var actionToSend : ActionsToSet? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_device)

        bind()
        deviceDetail = intent.getSerializableExtra("AccessoryDetail") as? HubAccessoryConfiguration
        init()

        setupDeviceInfos()
        setUpRv()
        handleViewByType()
        setObserverOnState()
        setObserverOnBrightness()
        setObserverOnColor()
        setObserverOnTemperature()
    }

    private fun bind() {
        detailDeviceName = findViewById(R.id.detail_device_title_tv)
        detailDeviceRoom = findViewById(R.id.detail_device_room_tv)
        configLinearLayout = findViewById(R.id.detail_device_config_linear_layout)
        configurateButton = findViewById(R.id.detail_device_config_button)
        stateLayout = findViewById(R.id.detail_device_state_linear_layout)
        stateSwitch = findViewById(R.id.detail_device_state_switch)
        brightnessLayout = findViewById(R.id.detail_device_brightness_linear_layout)
        brightnessSlider = findViewById(R.id.detail_device_brightness_seekbar)
        colorLayout = findViewById(R.id.detail_device_color_linear_layout)
        colorSlider = findViewById(R.id.detail_device_color_picker)
        colorTemperatureLayout = findViewById(R.id.detail_device_temperature_linear_layout)
        colorTemperatureSlider = findViewById(R.id.detail_device_temperature_seekbar)
        deviceDetailRecyclerView = findViewById(R.id.detail_device_specifications_rv)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        detailDeviceSpecificationInfoNames = ArrayList()
        detailDeviceKeyValue = HashMap()
        detailDeviceAdapter = DetailDeviceSpecificationAdapter()
        deviceVM = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceVM.context = this

        if(deviceDetail?.actions?.state != null) {
            stateSwitch.isChecked = deviceDetail?.actions?.state!! == DeviceState.on
        }

        configurateButton.setOnClickListener {
            val intent = Intent(this, SupportedAccessoriesListActivity::class.java)
            intent.putExtra("device", deviceDetail!!)
            startActivity(intent)
        }

        if(deviceDetail?.actions?.brightness != null) {
            brightnessSlider.min = deviceDetail?.actions?.brightness?.min_val!!
            brightnessSlider.max = deviceDetail?.actions?.brightness?.max_val!!
            brightnessSlider.progress = deviceDetail?.actions?.brightness?.current_state!!
        }

        if(deviceDetail?.actions?.color_temp != null) {
            colorTemperatureSlider.min = deviceDetail?.actions?.color_temp?.min_val!!
            colorTemperatureSlider.max = deviceDetail?.actions?.color_temp?.max_val!!
            colorTemperatureSlider.progress = deviceDetail?.actions?.color_temp?.current_state!!
        }
    }

    private fun setupDeviceInfos() {
        detailDeviceName.text = deviceDetail!!.name
        detailDeviceName.setTextColor(Color.parseColor(deviceDetail!!.background_color))
        detailDeviceRoom.text = deviceDetail!!.room?.name

        val manufacturerLabel = getString(R.string.specification_manufacturer)
        val modelLabel = getString(R.string.specification_model)
        val reachabilityLabel = getString(R.string.specification_reachable)

        val reachabilityStatus =  if (deviceDetail!!.is_reachable == true) getString(R.string.reachable_ok) else  getString(R.string.reachable_nok)

        detailDeviceSpecificationInfoNames.add(manufacturerLabel)
        detailDeviceSpecificationInfoNames.add(modelLabel)
        detailDeviceSpecificationInfoNames.add(reachabilityLabel)

        detailDeviceKeyValue[manufacturerLabel] = if(deviceDetail!!.manufacturer == null) "" else deviceDetail!!.manufacturer!!
        detailDeviceKeyValue[modelLabel] = if(deviceDetail!!.model == null) "" else deviceDetail!!.model!!
        detailDeviceKeyValue[reachabilityLabel] = reachabilityStatus
    }

    private fun setUpRv() {
        deviceDetailRecyclerView.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        )
        val dividerItemDecoration = DividerItemDecoration(
                deviceDetailRecyclerView.context,
                LinearLayoutManager.VERTICAL
        )
        deviceDetailRecyclerView.addItemDecoration(dividerItemDecoration)

        detailDeviceAdapter.deviceSpecificationInfoNames = detailDeviceSpecificationInfoNames
        detailDeviceAdapter.deviceSpecificationList = detailDeviceKeyValue
        deviceDetailRecyclerView.adapter = detailDeviceAdapter
    }

    private fun handleViewByType() {
        when(deviceDetail!!.type) {
            HubAccessoryType.lightbulb -> {
                configLinearLayout.visibility = View.GONE
                colorTemperatureLayout.visibility = if(deviceDetail!!.actions?.color_temp == null) View.GONE else View.VISIBLE
                colorLayout.visibility = if(deviceDetail!!.actions?.color == null) View.GONE else View.VISIBLE
            }
            HubAccessoryType.switch -> {
                configLinearLayout.visibility = View.GONE
                brightnessLayout.visibility = View.GONE
                colorLayout.visibility = View.GONE
                colorTemperatureLayout.visibility = View.GONE
            }
            HubAccessoryType.sensor -> {
                configLinearLayout.visibility = View.GONE
                brightnessLayout.visibility = View.GONE
                colorLayout.visibility = View.GONE
                colorTemperatureLayout.visibility = View.GONE
            }
            HubAccessoryType.occupancy -> {
                configLinearLayout.visibility = View.GONE
                stateLayout.visibility = View.GONE
                brightnessLayout.visibility = View.GONE
                colorLayout.visibility = View.GONE
                colorTemperatureLayout.visibility = View.GONE
            }
            HubAccessoryType.contact -> {
                configLinearLayout.visibility = View.GONE
                stateLayout.visibility = View.GONE
                brightnessLayout.visibility = View.GONE
                colorLayout.visibility = View.GONE
                colorTemperatureLayout.visibility = View.GONE
            }
            HubAccessoryType.unknown -> {
                stateLayout.visibility = View.GONE
                brightnessLayout.visibility = View.GONE
                colorLayout.visibility = View.GONE
                colorTemperatureLayout.visibility = View.GONE
            }
            else -> {
                stateLayout.visibility = View.GONE
                brightnessLayout.visibility = View.GONE
                colorLayout.visibility = View.GONE
                colorTemperatureLayout.visibility = View.GONE
            }
        }
    }

    private fun setObserverOnState() {
        stateSwitch.setOnCheckedChangeListener { _, isChecked ->
            val stateChanged =if(isChecked) DeviceState.on.toString() else DeviceState.off.toString()
            updateDeviceActions(state = stateChanged)
        }
    }

    private fun setObserverOnBrightness() {
        brightnessSlider.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                deviceDetail?.actions?.brightness?.current_state = seek.progress
                updateDeviceActions(brightness = seek.progress)
            }
        })
    }

    private fun setObserverOnColor() {
        colorSlider.setOnColorChangedListener(object :
                ColorPicker.OnColorChangedListener {
            override fun onColorChanged(color: Int) {
                updateDeviceActions(color = ColorAction(hex = String.format("#%06X", 0xFFFFFF and color)))
            }
        })
    }

    private fun setObserverOnTemperature() {
        colorTemperatureSlider.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                deviceDetail?.actions?.color_temp?.current_state = seek.progress
                updateDeviceActions(temp = seek.progress)
            }
        })
    }

    private fun updateDeviceActions(state : String? = null, brightness : Int? = null, color : ColorAction? = null, temp : Int? = null) {

        val actions = ActionsToSetIn(state = state, brightness = brightness, color = color, color_temp = temp)
        actionToSend = ActionsToSet(deviceDetail?.friendly_name!!, actions)
        deviceVM.sendDeviceAction(actionToSend!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_modifier_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.detail_modifier_btn -> {
            val deviceModificationIntent = Intent(this, CreateDeviceActivity::class.java)
            deviceModificationIntent.putExtra("device", deviceDetail!!)
            startActivity(deviceModificationIntent)
            true
        }



        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}

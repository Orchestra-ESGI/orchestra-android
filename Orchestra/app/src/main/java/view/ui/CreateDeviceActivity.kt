package view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.Actions
import core.rest.model.SupportedDeviceInformations
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.HubAccessoryType
import utils.OnItemClicked
import view.adapter.ShuffleColorAdapter
import kotlin.random.Random

class CreateDeviceActivity : AppCompatActivity(), OnItemClicked {

    private lateinit var titleTextView: TextView
    private lateinit var nameTitleTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var whichRoomTitleTextView: TextView
    private lateinit var whichRoomEditText: EditText
    private lateinit var chooseColorTitleTextView: TextView
    private lateinit var shuffleColorButton: ImageView
    private lateinit var deviceColorsRecyclerView: RecyclerView

    private lateinit var deviceColorsAdapter : ShuffleColorAdapter
    private lateinit var deviceColorsHashMap : MutableMap<Int, Boolean>
    private lateinit var deviceColors : ArrayList<Int>

    private var device : HubAccessoryConfiguration? = null
    private var supportedDevice : SupportedDeviceInformations? = null
    private var brand : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_device)

        bind()
        getIntentInfos()
        generateBackGroundColor()
        setUpRv()
        setUpShuffleColor()
    }

    private fun getIntentInfos() {
        device = intent.getSerializableExtra("device") as? HubAccessoryConfiguration
        supportedDevice = intent.getSerializableExtra("SupportedDevice") as? SupportedDeviceInformations
        brand = intent.getStringExtra("brand")

        nameEditText.setText(device?.name)
        whichRoomEditText.setText(device?.room_name)
    }

    private fun bind() {
        titleTextView = findViewById(R.id.create_device_new_device_tv)
        nameTitleTextView = findViewById(R.id.create_device_name_tv)
        nameEditText = findViewById(R.id.create_device_name_et)
        chooseColorTitleTextView = findViewById(R.id.create_device_choose_color_tv)
        shuffleColorButton = findViewById(R.id.create_device_shuffle_color_iv)
        deviceColorsRecyclerView = findViewById(R.id.create_device_colors_rv)
        whichRoomTitleTextView = findViewById(R.id.create_device_which_room_tv)
        whichRoomEditText = findViewById(R.id.create_device_which_room_et)
    }

    private fun generateBackGroundColor() {
        this.deviceColorsHashMap = mutableMapOf()
        this.deviceColors = ArrayList()
        val size = 5
        var randomColor : Int = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        this.deviceColorsHashMap[randomColor] = true
        this.deviceColors.add(randomColor)
        for (i in 1..size) {
            randomColor = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            this.deviceColorsHashMap[randomColor] = false
            this.deviceColors.add(randomColor)
        }
    }

    private fun setUpRv() {
        deviceColorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        deviceColorsAdapter = ShuffleColorAdapter(this)
        deviceColorsAdapter.colorListMap = deviceColorsHashMap
        deviceColorsAdapter.colorList = deviceColors
        deviceColorsRecyclerView.adapter = deviceColorsAdapter
    }

    private fun setUpShuffleColor() {
        shuffleColorButton.setOnClickListener {
            this.deviceColorsHashMap.clear()
            this.deviceColors.toMutableList().clear()
            generateBackGroundColor()
            redrawShuffleColors()
        }
    }

    private fun redrawShuffleColors() {
        deviceColorsRecyclerView.adapter = null
        deviceColorsAdapter.colorListMap = deviceColorsHashMap
        deviceColorsAdapter.colorList = deviceColors
        deviceColorsRecyclerView.adapter = deviceColorsAdapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun colorClicked(color: Int, position: Int) {
        val oldSelectedColor = deviceColorsHashMap.filterValues {
            it
        }
        if (oldSelectedColor.size == 1) {
            deviceColorsHashMap.replace(oldSelectedColor.keys.first(), false)
            deviceColorsHashMap.replace(color, true)
            redrawShuffleColors()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_device_menu, menu)
        return true
    }

    private fun retrieveData() : HubAccessoryConfiguration {
        val name = nameEditText.text.toString()
        val roomName = whichRoomEditText.text.toString()
        val backgroundColor = deviceColorsHashMap.filterValues { it }.keys.first()
        var type : HubAccessoryType?

        when (supportedDevice?.type) {
            "lightbulb" -> {type = HubAccessoryType.lightbulb}
            "switch" -> type = HubAccessoryType.switch
            "sensor" -> type = HubAccessoryType.sensor
            else -> { // Note the block
                type = device?.type
            }
        }

        val friendlyName = if(device == null) null else device!!.friendly_name

        return HubAccessoryConfiguration(name, roomName, String.format("#%06X", 0xFFFFFF and backgroundColor), brand, supportedDevice?.model, null, null, type, Actions(null, null, null, null), friendlyName)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_scene_add_btn -> {
            val intent = Intent()
            intent.putExtra("CreatedScene", retrieveData())
            if(supportedDevice == null) {
                // Faire appel Update
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
}
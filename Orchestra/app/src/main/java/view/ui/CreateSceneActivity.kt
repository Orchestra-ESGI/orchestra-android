package view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.*
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import utils.OnActionClicked
import utils.OnActionLongClicked
import utils.OnItemClicked
import view.adapter.CreateSceneActionsAdapter
import view.adapter.ShuffleColorAdapter
import viewModel.SceneViewModel
import kotlin.random.Random


class CreateSceneActivity : AppCompatActivity(), OnItemClicked, OnActionClicked, OnActionLongClicked {

    private lateinit var titleTextView: TextView
    private lateinit var nameTitleTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var chooseColorTitleTextView: TextView
    private lateinit var shuffleColorButton: ImageView
    private lateinit var sceneColorsRecyclerView: RecyclerView
    private lateinit var descriptionTitleTextView: TextView
    private lateinit var descriptionEditText: EditText
    private lateinit var addActionTextView: TextView
    private lateinit var listActionRecyclerView: RecyclerView

    private lateinit var sceneColorsAdapter : ShuffleColorAdapter
    private lateinit var listActionAdapter : CreateSceneActionsAdapter

    private lateinit var sceneColorsHashMap : MutableMap<Int, Boolean>
    private lateinit var sceneColors : ArrayList<Int>
    private var actionList : ArrayList<HubAccessoryConfiguration>  = ArrayList()

    private lateinit var sceneViewModel : SceneViewModel

    private var deviceList : ArrayList<HubAccessoryConfiguration> = ArrayList()
    private var availableDeviceList : ArrayList<HubAccessoryConfiguration> = ArrayList()

    private var sceneDetail : Scene? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_scene)


        getIntentInfos()
        bind()
        loadDataIfModeModify()
        generateBackGroundColor()
        setUpRv()
        setUpShuffleColor()
        setUpAddActions()
        setObserversOnEditText()
    }

    private fun getIntentInfos() {
        val intent = intent
        sceneDetail = intent.getSerializableExtra("scene") as? Scene
        deviceList = if(sceneDetail != null) {
            intent.getSerializableExtra("deviceList") as ArrayList<HubAccessoryConfiguration>
        } else {
            val args = intent.getBundleExtra("BUNDLE")
            args!!.getSerializable("ARRAYLIST") as ArrayList<HubAccessoryConfiguration>
        }
        availableDeviceList = deviceList
    }

    private fun bind() {
        titleTextView = findViewById(R.id.create_scene_new_scene_tv)
        nameTitleTextView = findViewById(R.id.create_scene_name_tv)
        nameEditText = findViewById(R.id.create_scene_name_et)
        chooseColorTitleTextView = findViewById(R.id.create_scene_choose_color_tv)
        shuffleColorButton = findViewById(R.id.create_scene_shuffle_color_iv)
        sceneColorsRecyclerView = findViewById(R.id.create_scene_colors_rv)
        descriptionTitleTextView = findViewById(R.id.create_scene_description_tv)
        descriptionEditText = findViewById(R.id.create_scene_description_et)
        addActionTextView = findViewById(R.id.create_scene_add_action_tv)
        listActionRecyclerView = findViewById(R.id.create_scene_list_action_rv)

        sceneViewModel = ViewModelProviders.of(this).get(SceneViewModel::class.java)
        sceneViewModel.context = this
    }

    private fun loadDataIfModeModify() {
        if(sceneDetail != null) {
            nameEditText.setText(sceneDetail!!.name)
            descriptionEditText.setText(sceneDetail!!.description)
            actionList = formatSceneToHubAccessoryConfiguration()
        }
    }

    private fun generateBackGroundColor() {
        this.sceneColorsHashMap = mutableMapOf()
        this.sceneColors = ArrayList()
        val size = 5
        var randomColor : Int = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        this.sceneColorsHashMap[randomColor] = true
        this.sceneColors.add(randomColor)
        for (i in 1..size) {
            randomColor = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            this.sceneColorsHashMap[randomColor] = false
            this.sceneColors.add(randomColor)
        }
    }

    private fun setUpRv() {

        sceneColorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sceneColorsAdapter = ShuffleColorAdapter(this)
        sceneColorsAdapter.colorListMap = sceneColorsHashMap
        sceneColorsAdapter.colorList = sceneColors
        sceneColorsRecyclerView.adapter = sceneColorsAdapter

        listActionRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listActionAdapter = CreateSceneActionsAdapter(this, this)
        listActionAdapter.detailSceneActions = actionList
        listActionRecyclerView.adapter = listActionAdapter
    }

    private fun setUpShuffleColor() {
        shuffleColorButton.setOnClickListener {
            this.sceneColorsHashMap.clear()
            this.sceneColors.toMutableList().clear()
            generateBackGroundColor()
            redrawShuffleColors()
        }
    }

    private fun setUpAddActions() {
        addActionTextView.setOnClickListener {
            val arrayAdapter = ArrayAdapter<String>(this@CreateSceneActivity, android.R.layout.select_dialog_item)
            availableDeviceList.forEach {
                arrayAdapter.add(it.name)
            }
            val alertDialog: AlertDialog = this@CreateSceneActivity.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    this.setTitle(R.string.create_scene_list_action_description)
                            .setAdapter(arrayAdapter) { _, which ->
                                if(which < availableDeviceList.size) {
                                    addSection(availableDeviceList[which])
                                }
                            }
                }
                builder.create()
            }
            alertDialog.show()
        }
    }

    private fun addSection(device: HubAccessoryConfiguration) {
            actionList.add(device)
            availableDeviceList.remove(device)
            actionList.add(HubAccessoryConfiguration())
            listActionAdapter.notifyDataSetChanged()
            listActionRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun addAction(device: HubAccessoryConfiguration, position: Int) {
        actionList.add(position, device)
        listActionAdapter.notifyDataSetChanged()
        listActionRecyclerView.adapter!!.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun colorClicked(color: Int, position: Int) {
        val oldSelectedColor = sceneColorsHashMap.filterValues {
            it
        }
        if (oldSelectedColor.size == 1) {
            sceneColorsHashMap.replace(oldSelectedColor.keys.first(), false)
            sceneColorsHashMap.replace(color, true)
            redrawShuffleColors()
        }
    }

    private fun redrawShuffleColors() {
        sceneColorsRecyclerView.adapter = null
        sceneColorsRecyclerView.layoutManager = null
        sceneColorsAdapter.colorListMap = sceneColorsHashMap
        sceneColorsAdapter.colorList = sceneColors
        sceneColorsRecyclerView.adapter = sceneColorsAdapter
        sceneColorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setObserversOnEditText() {

        val filter = InputFilter { source, start, end, dest, dstart, dend ->

            for (i in start until end) {
                if (Character.isWhitespace(source[i])) {
                    return@InputFilter ""
                }
            }

/*
            val speChat = "\\s+"
            val pattern: Pattern = Pattern.compile(speChat)
            val matcher: Matcher = pattern.matcher(source.toString())
            if (matcher.find()) return@InputFilter ""

 */
            null
        }

        nameEditText.filters = arrayOf(filter)
        descriptionEditText.filters = arrayOf(filter)
    }

    private fun retrieveData() : Scene {
        val id = sceneDetail?._id
        val name = nameEditText.text
        val description = descriptionEditText.text
        val backgroundColor = sceneColorsHashMap.filterValues { it }.keys.first()
        val colorPicked = String.format("#%06X", 0xFFFFFF and backgroundColor)

        val listActionToSet : ArrayList<ActionsToSet> = ArrayList()

        val distinctListOfDevice = actionList.filter { action -> action.friendly_name != null }
        val distinctListOfFriendlyName = distinctListOfDevice.map { action -> action.friendly_name }
        val indexOfDevices = distinctListOfDevice.map { action -> actionList.indexOf(action) }

        for (i in indexOfDevices.indices) {
            var state : String? = null
            var brightness : Int? = null
            var color : ColorAction? = null
            var temperature : Int? = null
            var actionsToSetIn : ActionsToSetIn?
            val nextIndex = if(i != indexOfDevices.size-1) indexOfDevices[i + 1] else actionList.size-1
            for(j in indexOfDevices[i] until nextIndex step 1) {
                if(actionList[j].actions != null && actionList[j].friendly_name == null) {
                    val actionItem = actionList[j]
                    if(state == null) state = actionItem.actions!!.state?.name
                    if(brightness == null) brightness = if(actionItem.actions!!.brightness != null) actionItem.actions!!.brightness!!.current_state else null
                    if(color == null) color = if(actionItem.actions!!.color != null) ColorAction(hex = actionItem.actions!!.color!!.hex) else null
                    if(temperature == null) temperature = if(actionItem.actions!!.color_temp != null) actionItem.actions!!.color_temp!!.current_state else null
                }
            }
            actionsToSetIn = ActionsToSetIn(state, brightness, color, temperature)
            val actionToSet = ActionsToSet(friendly_name = distinctListOfFriendlyName[i], actions = actionsToSetIn)
            listActionToSet.add(actionToSet)
        }
        return Scene(_id = id, name = name.toString(), description = description.toString(), color = colorPicked, devices = listActionToSet)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_scene_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_scene_add_btn -> {
            val intent = Intent()
            val scene = retrieveData()
            if (sceneNotEmpty(scene)) {
                intent.putExtra("CreatedScene", scene)
                if (scene._id == null) {
                    sceneViewModel.saveScene(scene)
                } else {
                    sceneViewModel.updateScene(scene)
                }
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Veuillez renseigner les informations manquantes", Toast.LENGTH_SHORT).show()
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun actionClicked(action: SceneActionsName, position: Int) {
        val actions = Actions(null, null, null, null)
        val device = HubAccessoryConfiguration(actions = actions)
        when(action.type) {
            "state" -> {
                when (action.value) {
                    "on" -> {
                        device.actions!!.state = DeviceState.on
                    }
                    "off" -> {
                        device.actions!!.state = DeviceState.off
                    }
                    "toggle" -> {
                        device.actions!!.state = DeviceState.toggle
                    }
                }
            }
            "brightness" -> {
                device.actions!!.brightness = SliderAction(current_state = action.value.toInt())
            }
            "color_temp" -> {
                device.actions!!.color_temp = SliderAction(current_state = action.value.toInt())
            }
            "color" -> {
                device.actions!!.color = ColorAction(action.value)
            }
            else -> {

            }
        }
        addAction(device, position)
    }

    private fun formatSceneToHubAccessoryConfiguration() : ArrayList<HubAccessoryConfiguration>{
        val listOfSceneDevices = sceneDetail?.devices
        val listOfDeviceFormatted : ArrayList<HubAccessoryConfiguration> = ArrayList()

        listOfSceneDevices?.forEach {
            val section = deviceList.first { device -> device.friendly_name == it.friendly_name }
            availableDeviceList.remove(section)
            listOfDeviceFormatted.add(section)
            if (it.actions?.state != null) {
                var state : DeviceState? = null
                when(it.actions!!.state) {
                    "on" -> state = DeviceState.on
                    "off" -> state = DeviceState.off
                    "toggle" -> state = DeviceState.toggle
                }
                listOfDeviceFormatted.add(HubAccessoryConfiguration(actions = Actions(state = state)))
            }
            if (it.actions?.brightness != null) {
                listOfDeviceFormatted.add(HubAccessoryConfiguration(actions = Actions(brightness = SliderAction(current_state = it.actions!!.brightness!!))))
            }
            if (it.actions?.color != null) {
                listOfDeviceFormatted.add(HubAccessoryConfiguration(actions = Actions(color = it.actions!!.color)))
            }
            if (it.actions?.color_temp != null) {
                listOfDeviceFormatted.add(HubAccessoryConfiguration(actions = Actions(color_temp = SliderAction(current_state = it.actions!!.color_temp!!))))
            }
            listOfDeviceFormatted.add(HubAccessoryConfiguration())
        }
        return listOfDeviceFormatted
    }

    override fun actionLongClicked(device: HubAccessoryConfiguration, position: Int, isAction: Boolean) {
        if(isAction) {
            actionList.removeAt(position)
            listActionAdapter.notifyDataSetChanged()
            listActionRecyclerView.adapter!!.notifyDataSetChanged()
        } else {
            val listActionToRemove : ArrayList<Int> = ArrayList()
            listActionToRemove.add(position)

            var otherSection = false
            actionList.forEachIndexed { index, _ ->
                if (index > position) {
                    if(actionList[index].friendly_name == null && !otherSection) {
                        listActionToRemove.add(index)
                    } else {
                        otherSection = true
                    }
                }
            }

            listActionToRemove.forEach { _ ->
                actionList.removeAt(position)
            }
            availableDeviceList.add(device)
            listActionAdapter.notifyDataSetChanged()
            listActionRecyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    private fun sceneNotEmpty(scene: Scene) : Boolean {

        if(scene.name == "") return false
        if(scene.color == null || scene.color == "") return false
        if(scene.description == "") return false
        if(scene.devices.isEmpty()) return false

        scene.devices.forEach {
            if (it.friendly_name == null || it.friendly_name == "" || it.actions == null) {
                return false
            } else if (it.actions?.state == null && it.actions?.brightness == null && it.actions?.color_temp == null && it.actions?.color == null) {
                return false
            }
        }

        return true
    }

}
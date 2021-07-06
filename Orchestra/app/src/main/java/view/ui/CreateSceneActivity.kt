package view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.*
import core.rest.model.hubConfiguration.Device
import core.rest.model.hubConfiguration.HubAccessoryType
import utils.OnActionClicked
import utils.OnActionLongClicked
import utils.OnItemClicked
import view.adapter.CreateSceneActionsAdapter
import viewModel.SceneViewModel
import kotlin.random.Random


class CreateSceneActivity : AppCompatActivity(), OnItemClicked, OnActionClicked, OnActionLongClicked, AdapterView.OnItemSelectedListener {

    private lateinit var nameTitleTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var descriptionTitleTextView: TextView
    private lateinit var descriptionEditText: EditText
    private lateinit var triggerLinearLayout: LinearLayout
    private lateinit var triggerTitleTextView: TextView
    private lateinit var triggerDeviceSpinner: Spinner
    private lateinit var triggerActionSpinner: Spinner
    private lateinit var triggerActionTypeSpinner: Spinner
    private lateinit var triggerOperatorSpinner: Spinner
    private lateinit var triggerValueEditText: EditText
    private lateinit var addActionTextView: TextView
    private lateinit var listActionRecyclerView: RecyclerView

    private lateinit var listActionAdapter : CreateSceneActionsAdapter

    private lateinit var sceneColorsHashMap : MutableMap<Int, Boolean>
    private lateinit var sceneColors : ArrayList<Int>
    private var actionList : ArrayList<Device>  = ArrayList()

    private lateinit var sceneViewModel : SceneViewModel

    private var deviceList : ArrayList<Device> = ArrayList()
    private var availableDeviceList : ArrayList<Device> = ArrayList()

    private val triggerableDeviceTypeList = listOf(
            HubAccessoryType.occupancy,
            HubAccessoryType.contact,
            HubAccessoryType.programmableswitch,
            HubAccessoryType.temperatureandhumidity
    )

    private val actionListForSensor = listOf(
            DeviceState.on.name,
            DeviceState.off.name

    )

    private val actionListForSwitch = listOf(
            DeviceState.single.name,
            DeviceState.double.name,
            DeviceState.long.name

    )

    private val actionTypeTemperatureAndHumidityList = listOf(
        "temperature",
        "humidity"
    )

    private val operatorListForTemperatureAndHumidity = listOf(
        ">",
        "<"
    )

    private lateinit var arrayAdapterAction : ArrayAdapter<String>
    private lateinit var arrayAdapterTypeAction : ArrayAdapter<String>
    private lateinit var arrayAdapterDevice : ArrayAdapter<String>
    private lateinit var arrayAdapterOperator : ArrayAdapter<String>

    private var sceneDetail : Scene? = null
    private var automationDetail : Automation? = null
    private var isAutomatisation : Boolean? = null
    private var initModificationAutomation : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_scene)

        getIntentInfos()
        bind()
        setupAutomationView()
        loadDataIfModeModify()
        generateBackGroundColor()
        setUpRv()
        setUpAddActions()
        setObserversOnEditText()
    }

    private fun getIntentInfos() {
        val intent = intent
        sceneDetail = intent.getSerializableExtra("scene") as? Scene
        automationDetail = intent.getSerializableExtra("automation") as? Automation
        isAutomatisation = intent.getSerializableExtra("isAutomatisation") as? Boolean
        deviceList = if(sceneDetail != null || automationDetail != null) {
            intent.getSerializableExtra("deviceList") as ArrayList<Device>
        } else {
            val args = intent.getBundleExtra("BUNDLE")
            args!!.getSerializable("ARRAYLIST") as ArrayList<Device>
        }

        deviceList.filter { device -> !triggerableDeviceTypeList.contains(device.type) }.forEach {
            availableDeviceList.add(it)
        }
    }

    private fun bind() {
        nameTitleTextView = findViewById(R.id.create_scene_name_tv)
        nameEditText = findViewById(R.id.create_scene_name_et)
        descriptionTitleTextView = findViewById(R.id.create_scene_description_tv)
        descriptionEditText = findViewById(R.id.create_scene_description_et)
        triggerLinearLayout = findViewById(R.id.create_scene_trigger_linear_layout)
        triggerTitleTextView = findViewById(R.id.create_scene_add_trigger_tv)
        triggerDeviceSpinner = findViewById(R.id.create_scene_trigger_device)
        triggerActionSpinner = findViewById(R.id.create_scene_trigger_action)
        triggerActionTypeSpinner = findViewById(R.id.create_scene_trigger_action_type)
        triggerOperatorSpinner = findViewById(R.id.create_scene_trigger_operator)
        triggerValueEditText = findViewById(R.id.create_scene_trigger_value)
        addActionTextView = findViewById(R.id.create_scene_add_action_tv)
        listActionRecyclerView = findViewById(R.id.create_scene_list_action_rv)

        sceneViewModel = ViewModelProviders.of(this).get(SceneViewModel::class.java)
        sceneViewModel.context = this
    }

    private fun setupAutomationView() {
        if (isAutomatisation == true) {
            title = getString(R.string.create_scene_new_automation)
            triggerLinearLayout.visibility = View.VISIBLE
            initModificationAutomation = true
            setUpRoomSpinner()
        } else {
            title = getString(R.string.create_scene_new_scene)
        }
    }

    private fun setUpRoomSpinner() {
        val triggerDeviceList = deviceList.filter { device -> triggerableDeviceTypeList.contains(device.type) }

        if (triggerDeviceList.isNotEmpty()) {
            arrayAdapterAction = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item)
            arrayAdapterTypeAction = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item)
            arrayAdapterDevice = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item)
            arrayAdapterOperator = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item)

            arrayAdapterDevice.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            arrayAdapterAction.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            arrayAdapterOperator.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            arrayAdapterTypeAction.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)


            triggerDeviceList.forEach {
                arrayAdapterDevice.add(it.name)
            }
            triggerDeviceSpinner.adapter = arrayAdapterDevice

            operatorListForTemperatureAndHumidity.forEach {
                arrayAdapterOperator.add(it)
            }
            triggerOperatorSpinner.adapter = arrayAdapterOperator


            val deviceSelected : Device? = if (automationDetail != null) {
                triggerDeviceList.firstOrNull { device -> device.friendly_name == automationDetail!!.trigger.friendly_name }
            } else {
                val deviceSelectedIndex = triggerDeviceSpinner.selectedItemPosition
                triggerDeviceList[deviceSelectedIndex]
            }

            if (deviceSelected?.type == HubAccessoryType.temperatureandhumidity) {
                triggerActionSpinner.visibility = View.GONE
                triggerOperatorSpinner.visibility = View.VISIBLE
                triggerValueEditText.visibility = View.VISIBLE
            } else {
                triggerActionSpinner.visibility = View.VISIBLE
                triggerOperatorSpinner.visibility = View.GONE
                triggerValueEditText.visibility = View.GONE
            }

            val triggerActionList = when (deviceSelected?.type) {
                HubAccessoryType.programmableswitch -> actionListForSwitch
                else -> actionListForSensor
            }

            val triggerActionTypeList = when (deviceSelected?.type) {
                HubAccessoryType.temperatureandhumidity -> actionTypeTemperatureAndHumidityList
                else -> listOf("state")
            }

            triggerActionTypeList.forEach {
                arrayAdapterTypeAction.add(it)
            }
            triggerActionTypeSpinner.adapter = arrayAdapterTypeAction

            triggerActionList.forEach {
                arrayAdapterAction.add(it)
            }
            triggerActionSpinner.adapter = arrayAdapterAction

            if (automationDetail != null) {
                val actionTypeSelected = triggerActionTypeList.firstOrNull() { action -> action == automationDetail!!.trigger.type.name}
                val actionSelected = triggerActionList.firstOrNull { action -> action == automationDetail!!.trigger.actions.state}
                val indexDevice = triggerDeviceList.indexOf(deviceSelected)
                triggerDeviceSpinner.setSelection(indexDevice)
                if (actionTypeSelected != null) {
                    if(actionTypeSelected == "temperature" || actionTypeSelected == "humidity") {
                        val indexOperator = operatorListForTemperatureAndHumidity.indexOf(automationDetail!!.trigger.actions.operator)
                        triggerOperatorSpinner.setSelection(indexOperator)
                        triggerValueEditText.setText(automationDetail!!.trigger.actions.state)
                    }
                } else {
                    val indexAction = triggerActionList.indexOf(actionSelected)
                    triggerActionSpinner.setSelection(indexAction)
                }
            }
            triggerDeviceSpinner.onItemSelectedListener = this
        } else {
            triggerLinearLayout.visibility = View.GONE
            Toast.makeText(this, getString(R.string.cannot_create_automation), Toast.LENGTH_LONG).show()
        }
    }

    private fun loadDataIfModeModify() {
        if(sceneDetail != null) {
            title = getString(R.string.create_scene_modify_scene)
            nameEditText.setText(sceneDetail!!.name)
            descriptionEditText.setText(sceneDetail!!.description)
            actionList = formatActionsToHubAccessoryConfiguration(sceneDetail!!.devices)
        }

        if(automationDetail != null) {
            isAutomatisation = true
            setupAutomationView()
            title = getString(R.string.create_scene_modify_automation)
            nameEditText.setText(automationDetail!!.name)
            descriptionEditText.setText(automationDetail!!.description)
            actionList = formatActionsToHubAccessoryConfiguration(automationDetail!!.targets)
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

        listActionRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listActionAdapter = CreateSceneActionsAdapter(this, this)
        listActionAdapter.detailSceneActions = actionList
        listActionRecyclerView.adapter = listActionAdapter
    }

    private fun setUpAddActions() {
        addActionTextView.setOnClickListener {
            if (availableDeviceList.isNotEmpty()) {
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
            } else {
                Toast.makeText(this, getString(R.string.create_scene_no_device_available), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addSection(device: Device) {
            actionList.add(device)
            availableDeviceList.remove(device)
            actionList.add(Device())
            listActionAdapter.notifyDataSetChanged()
            listActionRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun addAction(device: Device, position: Int) {
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
        }
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

    private fun retrieveDataAutomation() : Automation {
        val id = automationDetail?._id
        val name = nameEditText.text
        val description = descriptionEditText.text
        val backgroundColor = sceneColorsHashMap.filterValues { it }.keys.first()
        val colorPicked = String.format("#%06X", 0xFFFFFF and backgroundColor)
        val operator = if (triggerOperatorSpinner.visibility == View.VISIBLE) triggerOperatorSpinner.selectedItem as String else null

        val listActionToSet = retrieveActionListData()

        val deviceSelectedIndex = triggerDeviceSpinner.selectedItemPosition
        val deviceSelected = deviceList.filter { device -> triggerableDeviceTypeList.contains(device.type) }[deviceSelectedIndex]
        val actionSelected = if (triggerValueEditText.visibility == View.VISIBLE) triggerValueEditText.text.toString() else triggerActionSpinner.selectedItem as String
        val typeTemperatureOrHumidity = if (deviceSelected.type == HubAccessoryType.temperatureandhumidity) HubAccessoryType.valueOf(triggerActionTypeSpinner.selectedItem as String) else null
        val type = typeTemperatureOrHumidity ?: deviceSelected.type!!

        val action = ActionsToSetIn(state = actionSelected, brightness = null, color_temp = null, color = null, operator = operator)
        val trigger = Trigger(type = type, friendly_name = deviceSelected.friendly_name!!, actions = action)

        return Automation(_id = id, name = name.toString(), color = colorPicked, description = description.toString(), trigger = trigger, targets = listActionToSet)
    }

    private fun retrieveDataScene() : Scene {
        val id = sceneDetail?._id
        val name = nameEditText.text
        val description = descriptionEditText.text
        val backgroundColor = sceneColorsHashMap.filterValues { it }.keys.first()
        val colorPicked = String.format("#%06X", 0xFFFFFF and backgroundColor)

        val listActionToSet = retrieveActionListData()

        return Scene(_id = id, name = name.toString(), description = description.toString(), color = colorPicked, devices = listActionToSet)
    }

    private fun retrieveActionListData() : ArrayList<ActionsToSet> {
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

        return listActionToSet
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_scene_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_scene_add_btn -> {
            val intent = Intent()
            if (deviceList.isNotEmpty()) {
                if (isAutomatisation == true) {
                    val triggerDeviceList = deviceList.filter { device -> triggerableDeviceTypeList.contains(device.type) }
                    if (triggerDeviceList.isNotEmpty()) {
                        val automation = retrieveDataAutomation()
                        if (automationNotEmpty(automation)) {
                            if(automation._id == null) {
                                sceneViewModel.saveAutomation(automation)
                            } else {
                                sceneViewModel.updateAutomation(automation)
                            }
                            setResult(RESULT_OK, intent)
                            finish()
                        } else {
                            Toast.makeText(this, getString(R.string.create_scene_missing_information_toast), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.cannot_create_automation), Toast.LENGTH_LONG).show()
                    }
                } else {
                    val scene = retrieveDataScene()
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
                        Toast.makeText(this, getString(R.string.create_scene_missing_information_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if(isAutomatisation == true) {
                    Toast.makeText(this, getString(R.string.cannot_create_automation), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, getString(R.string.cannot_create_scene), Toast.LENGTH_LONG).show()
                }
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun actionClicked(action: SceneActionsName, position: Int) {
        val actions = Actions(null, null, null, null)
        val device = Device(actions = actions)
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

    private fun formatActionsToHubAccessoryConfiguration(listOfSceneDevices : List<ActionsToSet>?) : ArrayList<Device>{
        val listOfDeviceFormatted : ArrayList<Device> = ArrayList()

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
                listOfDeviceFormatted.add(Device(actions = Actions(state = state)))
            }
            if (it.actions?.brightness != null) {
                listOfDeviceFormatted.add(Device(actions = Actions(brightness = SliderAction(current_state = it.actions!!.brightness!!))))
            }
            if (it.actions?.color != null) {
                listOfDeviceFormatted.add(Device(actions = Actions(color = it.actions!!.color)))
            }
            if (it.actions?.color_temp != null) {
                listOfDeviceFormatted.add(Device(actions = Actions(color_temp = SliderAction(current_state = it.actions!!.color_temp!!))))
            }
            listOfDeviceFormatted.add(Device())
        }
        return listOfDeviceFormatted
    }

    override fun actionLongClicked(device: Device, position: Int, isAction: Boolean) {
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

    private fun automationNotEmpty(automation: Automation) : Boolean {
        if(automation.name == "") return false
        if(automation.color == "") return false
        if(automation.description == "") return false
        if(automation.targets.isEmpty()) return false
        if(automation.trigger.friendly_name == "") return false
        if(automation.trigger.actions.state == "") return false

        automation.targets.forEach {
            if (it.friendly_name == null || it.friendly_name == "" || it.actions == null) {
                return false
            } else if (it.actions?.state == null && it.actions?.brightness == null && it.actions?.color_temp == null && it.actions?.color == null) {
                return false
            }
        }
        return true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(!initModificationAutomation) {
            val triggerDeviceList = deviceList.filter { device -> triggerableDeviceTypeList.contains(device.type) }
            val deviceSelected = triggerDeviceList[position]

            if (deviceSelected.type == HubAccessoryType.temperatureandhumidity) {
                triggerActionSpinner.visibility = View.GONE
                triggerOperatorSpinner.visibility = View.VISIBLE
                triggerValueEditText.visibility = View.VISIBLE
            } else {
                triggerActionSpinner.visibility = View.VISIBLE
                triggerOperatorSpinner.visibility = View.GONE
                triggerValueEditText.visibility = View.GONE
            }

            val triggerActionList = when (deviceSelected.type) {
                HubAccessoryType.programmableswitch -> actionListForSwitch
                else -> actionListForSensor
            }

            val triggerActionTypeList = when (deviceSelected.type) {
                HubAccessoryType.temperatureandhumidity -> actionTypeTemperatureAndHumidityList
                else -> listOf("state")
            }

            arrayAdapterTypeAction.clear()
            triggerActionTypeList.forEach { arrayAdapterTypeAction.add(it) }
            triggerActionTypeSpinner.adapter = arrayAdapterTypeAction

            arrayAdapterAction.clear()
            triggerActionList.forEach { arrayAdapterAction.add(it) }
            triggerActionSpinner.adapter = arrayAdapterAction
        }
        initModificationAutomation = false
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}
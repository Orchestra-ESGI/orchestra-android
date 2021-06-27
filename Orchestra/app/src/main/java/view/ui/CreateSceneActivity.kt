package view.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_scene)

        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        deviceList = args!!.getSerializable("ARRAYLIST") as ArrayList<HubAccessoryConfiguration>
        availableDeviceList = deviceList

        bind()
        generateBackGroundColor()
        setUpRv()
        setUpShuffleColor()
        setUpAddActions()
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

    private fun retrieveData() : Scene {
        val name = nameEditText.text
        val description = descriptionEditText.text
        val backgroundColor = sceneColorsHashMap.filterValues { it }.keys.first()
        val color = String.format("#%06X", 0xFFFFFF and backgroundColor)

        var listActionToSet : ArrayList<ActionsToSet> = ArrayList()

        var distinctListOfDevice = actionList.filter { action -> action.friendly_name != null }
        var distinctListOfFriendlyName = distinctListOfDevice.map { action -> action.friendly_name }
        var indexOfDevices = distinctListOfDevice.map { action -> actionList.indexOf(action) }


        for (i in indexOfDevices.indices) {
            var state : String? = null
            var brightness : Int? = null
            var color : ColorAction? = null
            var temperature : Int? = null
            var actionsToSetIn : ActionsToSetIn?
            var nextIndex = if(i != indexOfDevices!!.size-1) indexOfDevices[i + 1] else actionList.size-1
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
            var actionToSet = ActionsToSet(friendly_name = distinctListOfFriendlyName[i], actions = actionsToSetIn)
            listActionToSet.add(actionToSet)
        }
/*
        actionList.forEach {
            var friendlyName = ""
            if(it.friendly_name != null) {
                friendlyName = it.friendly_name!!
            }
            if(it.friendly_name == null && it.actions != null) {
                val brightness = if(it.actions!!.brightness != null) it.actions!!.brightness!!.current_state else null
                val color = if(it.actions!!.color != null) ColorAction(hex = it.actions!!.color!!.hex) else null
                val temperature = if(it.actions!!.color_temp != null) it.actions!!.color_temp!!.current_state else null
                var actionsToSetIn = ActionsToSetIn(it.actions!!.state?.name, brightness, color, temperature)
                var actionToSet = ActionsToSet(friendly_name = friendlyName, actions = actionsToSetIn)
                listActionToSet.add(actionToSet)
            }
        }

 */

        return Scene(name = name.toString(), description = description.toString(), color = color, devices = listActionToSet)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_scene_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create_scene_add_btn -> {
            val intent = Intent()
            val scene = retrieveData()
            intent.putExtra("CreatedScene", scene)
            sceneViewModel.saveScene(scene)
            setResult(RESULT_OK, intent)
            finish()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun actionClicked(actionsName: SceneActionsName, position: Int) {
        var actions = Actions(null, null, null, null)
        var device = HubAccessoryConfiguration(actions = actions)
        if(actionsName.type != null) {
            when(actionsName.type) {
                "state" -> {
                    when(actionsName.value) {
                        "on" -> {
                            device.actions!!.state = DeviceState.on
                        }
                        "off" -> {
                            device.actions!!.state = DeviceState.off
                        }
                        "toggle" -> {
                            device.actions!!.state = DeviceState.off
                        }
                    }
                }
                "brightness" -> {
                    device.actions!!.brightness = SliderAction(current_state = actionsName.value.toInt())
                }
                "color_temp" -> {
                    device.actions!!.color_temp = SliderAction(current_state = actionsName.value.toInt())
                }
                "color" -> {
                    device.actions!!.color = ColorAction(actionsName.value)
                }
                else -> {

                }
            }
        }
        addAction(device, position)
    }

    override fun actionLongClicked(device: HubAccessoryConfiguration, position: Int, isAction: Boolean) {
        if(isAction) {
            actionList.removeAt(position)
            listActionAdapter.notifyDataSetChanged()
            listActionRecyclerView.adapter!!.notifyDataSetChanged()
        } else {
            var listActionToRemove : ArrayList<Int> = ArrayList()
            listActionToRemove.add(position)
            actionList.forEachIndexed { index, hubAccessory ->
                if (index > position) {
                    if(actionList[index].friendly_name == null) {
                        listActionToRemove.add(index)
                    } else {
                        return
                    }
                }
            }

            listActionToRemove.forEach {
                actionList.removeAt(position)
            }
            availableDeviceList.add(device)
            listActionAdapter.notifyDataSetChanged()
            listActionRecyclerView.adapter!!.notifyDataSetChanged()
        }
    }

}
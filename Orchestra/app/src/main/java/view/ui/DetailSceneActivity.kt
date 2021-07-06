package view.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.*
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import view.adapter.DetailSceneActionsAdapter

class DetailSceneActivity : AppCompatActivity() {

    private lateinit var detailSceneName : TextView
    private lateinit var detailSceneDescription : TextView
    private lateinit var detailSceneTriggerTextView: TextView
    private lateinit var deviceSceneRecyclerView: RecyclerView
    private lateinit var detailSceneAdapter : DetailSceneActionsAdapter

    private var sceneDetail : Scene? = null
    private var automationDetail : Automation? = null
    private var listDevice : ArrayList<HubAccessoryConfiguration>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_scene)

        bind()
        getIntentInformation()
        setupSceneView()
        setupAutomationView()
        setupActionsRecyclerView()
    }

    private fun bind() {
        detailSceneName = findViewById(R.id.detail_scene_title_tv)
        detailSceneDescription = findViewById(R.id.detail_scene_description_tv)
        detailSceneTriggerTextView = findViewById(R.id.detail_scene_trigger_tv)
        deviceSceneRecyclerView = findViewById(R.id.detail_scene_actions_rv)

        detailSceneAdapter = DetailSceneActionsAdapter()
    }

    private fun getIntentInformation() {
        sceneDetail = intent.getSerializableExtra("DetailScene") as? Scene
        automationDetail = intent.getSerializableExtra("DetailAutomation") as? Automation
        val args = intent.getBundleExtra("BUNDLE")
        listDevice = args!!.getSerializable("ARRAYLIST") as ArrayList<HubAccessoryConfiguration>
    }

    private fun setupSceneView() {
        if (sceneDetail != null) {
            title = getString(R.string.detail_scene_title_scene)
            detailSceneTriggerTextView.visibility = View.GONE
            detailSceneName.text = sceneDetail!!.name
            detailSceneName.setTextColor(Color.parseColor(sceneDetail!!.color))
            detailSceneDescription.text = sceneDetail!!.description
            detailSceneAdapter.detailSceneActions = formatSceneToDetailSceneActions(sceneDetail!!.devices, listDevice!!)
        }
    }

    private fun setupAutomationView() {
        if (automationDetail != null) {
            title = getString(R.string.detail_scene_title_automation)
            detailSceneTriggerTextView.visibility = View.VISIBLE
            detailSceneName.text = automationDetail!!.name
            detailSceneName.setTextColor(Color.parseColor(automationDetail!!.color))
            detailSceneDescription.text = automationDetail!!.description
            val triggerDevice = listDevice?.firstOrNull { device -> device.friendly_name == automationDetail!!.trigger.friendly_name }
            if (triggerDevice != null) {
                detailSceneTriggerTextView.text = getString(R.string.detail_scene_automation_trigger, triggerDevice.name, automationDetail!!.trigger.actions.state)
            }
            detailSceneAdapter.detailSceneActions = formatSceneToDetailSceneActions(automationDetail!!.targets, listDevice!!)
        }
    }

    private fun setupActionsRecyclerView() {
        deviceSceneRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(
                deviceSceneRecyclerView.context,
                LinearLayoutManager.VERTICAL
        )
        deviceSceneRecyclerView.addItemDecoration(dividerItemDecoration)

        detailSceneAdapter.availableDeviceList = listDevice
        deviceSceneRecyclerView.adapter = detailSceneAdapter
    }

    private fun formatSceneToDetailSceneActions(listOfSceneDevices : List<ActionsToSet>, listDevice : ArrayList<HubAccessoryConfiguration>) : ArrayList<HubAccessoryConfiguration> {
        var listOfDeviceFormatted : ArrayList<HubAccessoryConfiguration> = ArrayList()

        listOfSceneDevices.forEach {
            val deviceName = listDevice.first { device -> device.friendly_name == it.friendly_name }
            val section = (HubAccessoryConfiguration(friendly_name = deviceName.friendly_name, name = deviceName.name))
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
        }
        return listOfDeviceFormatted
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_modifier_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.detail_modifier_btn -> {
            val sceneModificationIntent = Intent(this, CreateSceneActivity::class.java)
            if(sceneDetail != null) sceneModificationIntent.putExtra("scene", sceneDetail)
            if(automationDetail != null) sceneModificationIntent.putExtra("automation", automationDetail)

            sceneModificationIntent.putExtra("deviceList", listDevice!!)
            startActivity(sceneModificationIntent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
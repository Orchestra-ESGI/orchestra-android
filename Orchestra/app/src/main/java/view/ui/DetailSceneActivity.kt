package view.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.*
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import view.adapter.CreateSceneActionsAdapter
import view.adapter.DetailSceneActionsAdapter

class DetailSceneActivity : AppCompatActivity() {

    private lateinit var detailSceneName : TextView
    private lateinit var detailSceneDescription : TextView
    private lateinit var deviceSceneRecyclerView: RecyclerView
    private lateinit var detailSceneAdapter : DetailSceneActionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_scene)

        detailSceneName = findViewById(R.id.detail_scene_title_tv)
        detailSceneDescription = findViewById(R.id.detail_scene_description_tv)
        deviceSceneRecyclerView = findViewById(R.id.detail_scene_actions_rv)

        detailSceneAdapter = DetailSceneActionsAdapter()

        deviceSceneRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(
                deviceSceneRecyclerView.context,
                LinearLayoutManager.VERTICAL
        )
        deviceSceneRecyclerView.addItemDecoration(dividerItemDecoration)

        val sceneDetail = intent.getSerializableExtra("DetailScene") as? Scene
        val args = intent.getBundleExtra("BUNDLE")
        val listDevice = args!!.getSerializable("ARRAYLIST") as ArrayList<HubAccessoryConfiguration>

        detailSceneName.text = sceneDetail!!.name

        sceneDetail.color?.let {
            val sceneBackgroundColor = if(it.first() == '#') {
                Color.parseColor(it)
            } else {
                it.toInt()
            }
            detailSceneName.setTextColor(sceneBackgroundColor)
        }
        detailSceneDescription.text = sceneDetail.description

        detailSceneAdapter.detailSceneActions = formatSceneToDetailSceneActions(sceneDetail, listDevice)


        deviceSceneRecyclerView.adapter = detailSceneAdapter
    }

    private fun formatSceneToDetailSceneActions(scene : Scene, listDevice : ArrayList<HubAccessoryConfiguration>) : ArrayList<HubAccessoryConfiguration> {
        var listOfSceneDevices = scene.devices
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
}
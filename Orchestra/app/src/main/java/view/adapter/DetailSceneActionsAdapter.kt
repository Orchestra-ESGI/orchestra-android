package view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.SceneActionsName
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import java.util.ArrayList

class DetailSceneActionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val DEVICE = 0
        private const val ACTION = 1
    }

    private lateinit var layoutInflater: LayoutInflater
    var detailSceneActions: ArrayList<HubAccessoryConfiguration>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        if(detailSceneActions != null) {
            if(detailSceneActions!![position].friendly_name != null) return DEVICE else ACTION
        }
        return -1
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder {
        when(viewType) {
            DEVICE -> {
                layoutInflater = LayoutInflater.from(parent.context)
                val detailSceneActionView = layoutInflater.inflate(R.layout.cell_detail_scene_device, parent, false)
                return DetailDeviceViewHolder(detailSceneActionView)
            }
            ACTION -> {
                layoutInflater = LayoutInflater.from(parent.context)
                val detailSceneDeviceView = layoutInflater.inflate(R.layout.cell_detail_scene_action, parent, false)
                return DetailActionViewHolder(detailSceneDeviceView)
            }
            else -> {
                layoutInflater = LayoutInflater.from(parent.context)
                val detailSceneActionView = layoutInflater.inflate(R.layout.cell_detail_scene_action, parent, false)
                return DetailActionViewHolder(detailSceneActionView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (detailSceneActions!![position].friendly_name != null) {
            (holder as DetailDeviceViewHolder).bind(device = detailSceneActions!![position])
        } else {
            val actionsName = getListSceneDeviceName()
            (holder as DetailActionViewHolder).bind(device = detailSceneActions!![position], actionsName = actionsName, position = position)
        }
    }

    override fun getItemCount(): Int {
        if (detailSceneActions == null || detailSceneActions == null) {
            return 0
        }
        return detailSceneActions!!.size
    }

    class DetailDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val deviceName = itemView.findViewById<TextView>(R.id.cell_detail_scene_device_name)

        fun bind(device : HubAccessoryConfiguration) {
            if(device.friendly_name != null) {
                deviceName.text = device.name
            } else {
                deviceName.text = "Appareil Inconnu"
            }
        }
    }

    class DetailActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionName = itemView.findViewById<TextView>(R.id.cell_detail_scene_action_name)

        fun bind(device: HubAccessoryConfiguration, actionsName: ArrayList<SceneActionsName>, position: Int) {
            var name : String? = null
            if(device.actions?.state != null) {
                name = actionsName.firstOrNull { actionName -> actionName.value == device.actions!!.state!!.name && actionName.type == "state" }?.key
            } else if(device.actions?.brightness != null) {
                name = actionsName.firstOrNull { actionName -> actionName.value == device.actions!!.brightness!!.current_state.toString() && actionName.type == "brightness" }?.key
            } else if(device.actions?.color != null) {
                name = actionsName.firstOrNull { actionName -> actionName.value == device.actions!!.color!!.hex && actionName.type == "color" }?.key
            } else if(device.actions?.color_temp != null) {
                name = actionsName.firstOrNull { actionName -> actionName.value == device.actions!!.color_temp!!.current_state.toString() && actionName.type == "color_temp" }?.key
            } else {
                name = "Action Inconnu"
            }
            actionName.text = name
        }
    }

    private fun getListSceneDeviceName() : ArrayList<SceneActionsName> {
        var actions: ArrayList<String>
        var values: ArrayList<String>
        var actionsName = ArrayList<SceneActionsName>()

        actions = arrayListOf("Allumer l'appareil", "Éteindre l'appareil", "Basculer")
        values = arrayListOf("on", "off", "toggle")
        for (index in actions.indices) {
            val action = SceneActionsName(key = actions[index], value = values[index], type = "state")
            actionsName.add(action)
        }

        actions = arrayListOf("Régler la luminosité à 25%", "Régler la luminosité à 50%", "Régler la luminosité à 100%")
        values = arrayListOf("25", "50", "100")
        for (index in actions.indices) {
            val action = SceneActionsName(key = actions[index], value = values[index], type = "brightness")
            actionsName.add(action)
        }

        actions = arrayListOf("Choisir une couleur")
        values = arrayListOf("#FF0000")
        for (index in actions.indices) {
            val action = SceneActionsName(key = actions[index], value = values[index], type = "color")
            actionsName.add(action)
        }

        actions = arrayListOf("Choisir la température")
        values = arrayListOf("200")
        for (index in actions.indices) {
            val action = SceneActionsName(key = actions[index], value = values[index], type = "color_temp")
            actionsName.add(action)
        }

        return actionsName
    }
}
package view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
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
            val actionsName = getListSceneDeviceName(detailSceneActions!![position])
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
                deviceName.text = itemView.context.getString(R.string.detail_scene_action_unknown_device)
            }
        }
    }

    class DetailActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionName = itemView.findViewById<TextView>(R.id.cell_detail_scene_action_name)
        private val actionIv = itemView.findViewById<ImageView>(R.id.cell_detail_scene_action_iv)

        fun bind(device: HubAccessoryConfiguration, actionsName: ArrayList<SceneActionsName>, position: Int) {
            var name : String?
            actionIv.visibility = View.GONE

            if(device.actions?.state != null) {
                name = actionsName.firstOrNull { actionName -> actionName.value == device.actions!!.state!!.name && actionName.type == "state" }?.key
            } else if(device.actions?.brightness != null) {
                name = actionsName.firstOrNull { actionName -> actionName.value == device.actions!!.brightness!!.current_state.toString() && actionName.type == "brightness" }?.key
            } else if(device.actions?.color != null) {
                val hex = actionsName!!.filter { elem -> device.actions!!.color!!.hex == elem.value }
                name = ""
                if(hex.isNotEmpty()){
                    name = hex[0].key

                    val actionDrawable = AppCompatResources.getDrawable(
                            actionIv.context,
                            R.drawable.create_scene_shuffle_color_shape
                    )
                    val color = Color.parseColor(hex[0].value)
                    actionIv.setImageDrawable(actionDrawable)
                    DrawableCompat.setTint(actionIv.drawable,color);
                    actionIv.visibility = View.VISIBLE
                }
            } else if(device.actions?.color_temp != null) {
                name = actionsName.firstOrNull { actionName -> actionName.value == device.actions!!.color_temp!!.current_state.toString() && actionName.type == "color_temp" }?.key
            } else {
                name = itemView.context.getString(R.string.detail_scene_action_unknown_action)
            }
            actionName.text = name
        }
    }

    private fun getListSceneDeviceName(deviceAction: HubAccessoryConfiguration) : ArrayList<SceneActionsName> {
        var actions: ArrayList<String>
        var values: ArrayList<String>
        var actionsName = ArrayList<SceneActionsName>()

        if (deviceAction.actions?.color != null) {
            actionsName.add(SceneActionsName(key = "Changer la couleur à", value = "${deviceAction.actions?.color?.hex}", type = "color"))
        }

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

        actions = arrayListOf("Température à 25%", "Température à 50%", "Température à 100%")
        values = arrayListOf("25", "50", "100")
        for (index in actions.indices) {
            val action = SceneActionsName(key = actions[index], value = values[index], type = "color_temp")
            actionsName.add(action)
        }

        actions = arrayListOf("Choisir une couleur")
        values = arrayListOf("#FF0000")
        for (index in actions.indices) {
            val action = SceneActionsName(key = actions[index], value = values[index], type = "color")
            actionsName.add(action)
        }

        return actionsName
    }
}
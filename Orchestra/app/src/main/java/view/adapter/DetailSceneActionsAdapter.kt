package view.adapter

import android.content.Context
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

    var availableDeviceList : ArrayList<HubAccessoryConfiguration>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
            val section = getSectionTypeViaElementPosition(position)
            if(section != null) {
                val deviceParent = getDeviceParent(section.friendly_name!!)
                val actionsName = getListSceneDeviceName(detailSceneActions!![position], deviceParent, holder.itemView.context)
                (holder as DetailActionViewHolder).bind(device = detailSceneActions!![position], actionsName = actionsName, position = position)
            }
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

    private fun getListSceneDeviceName(deviceAction: HubAccessoryConfiguration, deviceParent: HubAccessoryConfiguration?, context: Context) : ArrayList<SceneActionsName> {
        var actions: ArrayList<String>
        var values: ArrayList<String>
        var actionsName = ArrayList<SceneActionsName>()

        if (deviceAction.actions?.color != null) {
            actionsName.add(SceneActionsName(key = context.getString(R.string.create_scene_actions_adapter_choosen_color), value = "${deviceAction.actions?.color?.hex}", type = "color"))
        }

        if(deviceParent?.actions?.brightness != null) {
            actions = arrayListOf(context.getString(R.string.create_scene_actions_adapter_device_state_on), context.getString(R.string.create_scene_actions_adapter_device_state_off), context.getString(R.string.create_scene_actions_adapter_device_state_toggle))
            values = arrayListOf("on", "off", "toggle")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "state")
                actionsName.add(action)
            }
        }

        if (deviceParent?.actions?.brightness != null) {
            val maxValBrightness = deviceParent?.actions?.brightness?.max_val!!
            actions = arrayListOf(context.getString(R.string.create_scene_actions_adapter_device_brightness_25), context.getString(R.string.create_scene_actions_adapter_device_brightness_50), context.getString(R.string.create_scene_actions_adapter_device_brightness_75), context.getString(R.string.create_scene_actions_adapter_device_brightness_100))
            values = arrayListOf("${maxValBrightness.div(4)}", "${maxValBrightness.div(2)}" ,"${3*(maxValBrightness.div(4))}" ,"$maxValBrightness")

            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "brightness")
                actionsName.add(action)
            }
        }

        if (deviceParent?.actions?.color_temp != null) {
            val maxValTemp = deviceParent?.actions?.color_temp?.max_val!!
            actions = arrayListOf(context.getString(R.string.create_scene_actions_adapter_device_temperature_25), context.getString(R.string.create_scene_actions_adapter_device_temperature_50), context.getString(R.string.create_scene_actions_adapter_device_temperature_75), context.getString(R.string.create_scene_actions_adapter_device_temperature_100))
            values = arrayListOf("${maxValTemp.div(4)}", "${maxValTemp.div(2)}" , "${(3*(maxValTemp.div(4)))}" ,"$maxValTemp")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "color_temp")
                actionsName.add(action)
            }
        }

        if(deviceParent?.actions?.color != null) {
            actions = arrayListOf(context.getString(R.string.create_scene_actions_adapter_choose_color))
            values = arrayListOf("#FF0000")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "color")
                actionsName.add(action)
            }
        }

        return actionsName
    }


    private fun getSectionTypeViaElementPosition(position: Int) : HubAccessoryConfiguration? {
        for (i in (detailSceneActions!!.size - 1) downTo 0 step 1) {
            if(detailSceneActions!![i].friendly_name != null) {
                if(i <= position) {
                    return detailSceneActions!![i]
                }
            }
        }
        return null
    }

    private fun getDeviceParent(friendlyName : String) : HubAccessoryConfiguration? {
        return availableDeviceList?.firstOrNull { device -> device.friendly_name == friendlyName }
    }
}
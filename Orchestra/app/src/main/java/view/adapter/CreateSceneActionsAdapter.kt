package view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.SceneActionsName
import core.rest.model.hubConfiguration.Device
import core.utils.ColorPicker
import utils.OnActionClicked
import utils.OnActionLongClicked
import java.util.ArrayList

class CreateSceneActionsAdapter(onActionClicked: OnActionClicked, onActionLongClicked: OnActionLongClicked): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val DEVICE = 0
        private const val ACTION = 1
    }

    private lateinit var layoutInflater: LayoutInflater
    private val itemClickListener: OnActionClicked = onActionClicked
    private val itemLongClickListener: OnActionLongClicked = onActionLongClicked

    var detailSceneActions: ArrayList<Device>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        if(detailSceneActions != null) {
            return if(detailSceneActions!![position].friendly_name != null) DEVICE else ACTION
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
                return DetailSceneDevicesViewHolder(detailSceneActionView)
            }
            ACTION -> {
                layoutInflater = LayoutInflater.from(parent.context)
                val detailSceneDeviceView = layoutInflater.inflate(R.layout.cell_detail_scene_action, parent, false)
                return DetailSceneActionsViewHolder(detailSceneDeviceView)
            }
            else -> {
                layoutInflater = LayoutInflater.from(parent.context)
                val detailSceneActionView = layoutInflater.inflate(R.layout.cell_detail_scene_action, parent, false)
                return DetailSceneActionsViewHolder(detailSceneActionView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (detailSceneActions!![position].friendly_name != null) {
            (holder as DetailSceneDevicesViewHolder).bind(detailSceneActions!![position], position, itemLongClickListener)
        } else {
            val section = getSectionTypeViaElementPosition(position)
            val actionsName = parseDeviceActionToGetName(device = section!!, deviceAction = detailSceneActions!![position], context = holder.itemView.context)
            val alreadySelectedActionTypes = getAlreadySelectedActionTypesForDevice(position)
            (holder as DetailSceneActionsViewHolder).bind(detailSceneActions!![position], position, alreadySelectedActionTypes, actionsName, itemClickListener, itemLongClickListener)
        }
    }

    override fun getItemCount(): Int {
        if (detailSceneActions == null) {
            return 0
        }
        return detailSceneActions!!.size
    }

    class DetailSceneActionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionTv = itemView.findViewById<TextView>(R.id.cell_detail_scene_action_name)
        private val actionIv = itemView.findViewById<ImageView>(R.id.cell_detail_scene_action_iv)

        fun bind(action : Device, position : Int, usedActionTypes : List<Device>, actionsName : ArrayList<SceneActionsName>?, listener : OnActionClicked, longClicked: OnActionLongClicked) {
            actionIv.visibility = View.GONE
            if(action.actions == null && action.friendly_name == null) {
                actionTv.text = itemView.context.getString(R.string.create_scene_add_action)
            } else {
                if(action.actions?.state != null) {
                    val state = actionsName!!.filter { elem -> action.actions!!.state!!.name == elem.value }
                    actionTv.text = if(state.isNotEmpty()) state[0].key else ""
                } else if (action.actions?.brightness?.current_state != null) {
                    val brightnessTypes = actionsName!!.filter { elem -> elem.type == "brightness"}
                    val brightness = brightnessTypes!!.filter { elem -> action.actions!!.brightness!!.current_state == elem.value.toInt()}
                    actionTv.text = if(brightness.isNotEmpty()) brightness[0].key else ""
                } else if (action.actions?.color?.hex != null) {
                    val hex = actionsName!!.filter { elem -> action.actions!!.color!!.hex == elem.value }
                    actionTv.text = ""
                    if(hex.isNotEmpty()){
                        actionTv.text = hex[0].key

                        val actionDrawable = AppCompatResources.getDrawable(
                                actionIv.context,
                                R.drawable.create_scene_shuffle_color_shape
                        )
                        val color = Color.parseColor(hex[0].value)
                        actionIv.setImageDrawable(actionDrawable)
                        DrawableCompat.setTint(actionIv.drawable,color);
                        actionIv.visibility = View.VISIBLE
                    }
                } else if (action.actions?.color_temp?.current_state != null) {
                    val temperatureTypes = actionsName!!.filter { elem -> elem.type == "color_temp"}
                    val temperature = temperatureTypes!!.filter { elem -> action.actions!!.color_temp!!.current_state == elem.value.toInt() }
                    actionTv.text = if(temperature.isNotEmpty()) temperature[0].key else ""
                } else {
                    actionTv.text = itemView.context.getString(R.string.create_scene_actions_adapter_error)
                }
            }

            val actionTypes = usedActionTypes.map { action -> action.actions }
            actionTypes.forEach {
                if(it?.state != null) {
                    actionsName?.removeAll { scene -> scene.type == "state" }
                }
                if(it?.brightness != null) {
                    actionsName?.removeAll { scene -> scene.type == "brightness" }
                }
                if(it?.color != null) {
                    actionsName?.removeAll { scene -> scene.type == "color" }
                }
                if(it?.color_temp != null) {
                    actionsName?.removeAll { scene -> scene.type == "color_temp" }
                }
            }

            val arrayAdapter = ArrayAdapter<String>(itemView.context, android.R.layout.select_dialog_item)
            actionsName!!.forEach {
                arrayAdapter.add(it.key)
            }

            itemView.setOnClickListener {
                if (actionsName.isNotEmpty()) {
                    if (action.actions == null && action.friendly_name == null) {
                        val alertDialog: AlertDialog = itemView.context.let {
                            val builder = AlertDialog.Builder(it)
                            builder.apply {
                                this.setTitle(R.string.create_scene_add_action_text)
                                setAdapter(arrayAdapter) { _, which ->
                                    if (actionsName[which].key == itemView.context.getString(R.string.create_scene_actions_adapter_choose_color)) {
                                        val view = LayoutInflater.from(itemView.context).inflate(R.layout.custom_view_color_picker, null)
                                        val colorPicker = view.findViewById<ColorPicker>(R.id.custom_view_color_picker)
                                        var colorSelected = String.format("#%06X", 0xFFFFFF and colorPicker.color)
                                        colorPicker.setOnColorChangedListener(object :
                                                ColorPicker.OnColorChangedListener {
                                            override fun onColorChanged(color: Int) {
                                                colorSelected = String.format("#%06X", 0xFFFFFF and color)
                                            }
                                        })
                                        val builder = AlertDialog.Builder(itemView.context)
                                                .setTitle(itemView.context.getString(R.string.create_scene_actions_adapter_choose_color))
                                                .setPositiveButton(itemView.context.getString(R.string.create_scene_actions_adapter_ok)) { _, which ->
                                                    listener.actionClicked(SceneActionsName(type = "color", key = itemView.context.getString(R.string.create_scene_actions_adapter_choosen_color), value = colorSelected), position = position)
                                                }
                                                .setNegativeButton(itemView.context.getString(R.string.create_scene_actions_adapter_cancel)) { dialog, _ ->
                                                    dialog.cancel()
                                                }
                                                .create()
                                        builder.setView(view)
                                        builder.setCanceledOnTouchOutside(false)
                                        builder.show()
                                    } else {
                                        listener.actionClicked(actionsName[which], position = position)
                                    }
                                }
                            }
                            builder.create()
                        }
                        alertDialog.show()
                    }
                } else {
                        Toast.makeText(itemView.context, itemView.context.getString(R.string.create_scene_no_action_available), Toast.LENGTH_LONG).show()
                }
            }


            itemView.setOnLongClickListener {
                if(actionTv.text != itemView.context.getString(R.string.create_scene_add_action)) {
                    AlertDialog.Builder(itemView.context)
                            .setTitle(itemView.context.getString(R.string.create_scene_actions_adapter_delete_action))
                            .setMessage(itemView.context.getString(R.string.create_scene_actions_adapter_delete_action_confirmation))
                            .setPositiveButton(R.string.create_scene_delete_action) { _, _ ->
                                longClicked.actionLongClicked(action, position, true)
                            }
                            .setNegativeButton(R.string.create_scene_cancel) { dialog, _ ->
                                dialog.cancel()
                            }
                            .create()
                            .show()
                }
                true
            }
        }
    }

    class DetailSceneDevicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionTv = itemView.findViewById<TextView>(R.id.cell_detail_scene_device_name)

        fun bind(action : Device, position: Int, itemLongClickListener: OnActionLongClicked) {
            actionTv.text = action.name

            itemView.setOnLongClickListener {
                AlertDialog.Builder(itemView.context)
                        .setTitle(itemView.context.getString(R.string.create_scene_actions_adapter_delete_device))
                        .setMessage(itemView.context.getString(R.string.create_scene_actions_adapter_delete_device_confirmation))
                        .setPositiveButton(R.string.create_scene_delete_action) { _, _ ->
                            itemLongClickListener.actionLongClicked(action, position, false)
                        }
                        .setNegativeButton(R.string.create_scene_cancel) {dialog, _ ->
                            dialog.cancel()
                        }
                        .create()
                        .show()
                true
            }
        }
    }

    private fun parseDeviceActionToGetName(device: Device, deviceAction: Device, context: Context) : ArrayList<SceneActionsName> {
        var actions: ArrayList<String>
        var values: ArrayList<String>
        var actionsName = ArrayList<SceneActionsName>()

        if (deviceAction.actions?.color != null) {
            actionsName.add(SceneActionsName(key = context.getString(R.string.create_scene_actions_adapter_choosen_color), value = "${deviceAction.actions?.color?.hex}", type = "color"))
        }

        if (device.actions?.state != null) {
            actions = arrayListOf(context.getString(R.string.create_scene_actions_adapter_device_state_on), context.getString(R.string.create_scene_actions_adapter_device_state_off), context.getString(R.string.create_scene_actions_adapter_device_state_toggle))
            values = arrayListOf("on", "off", "toggle")

            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "state")
                actionsName.add(action)
            }
        }

        if(device.actions?.brightness != null){
            val maxValBrightness = device.actions?.brightness?.max_val!!
            actions = arrayListOf(context.getString(R.string.create_scene_actions_adapter_device_brightness_25), context.getString(R.string.create_scene_actions_adapter_device_brightness_50), context.getString(R.string.create_scene_actions_adapter_device_brightness_75), context.getString(R.string.create_scene_actions_adapter_device_brightness_100))
            values = arrayListOf("${maxValBrightness.div(4)}", "${maxValBrightness.div(2)}" ,"${3*(maxValBrightness.div(4))}" ,"$maxValBrightness")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "brightness")
                actionsName.add(action)
            }
        }

        if(device.actions?.color_temp != null){
            val maxValTemp = device.actions?.color_temp?.max_val!!
            actions = arrayListOf(context.getString(R.string.create_scene_actions_adapter_device_temperature_25), context.getString(R.string.create_scene_actions_adapter_device_temperature_50), context.getString(R.string.create_scene_actions_adapter_device_temperature_75), context.getString(R.string.create_scene_actions_adapter_device_temperature_100))
            values = arrayListOf("${maxValTemp.div(4)}", "${maxValTemp.div(2)}" ,"${(3*(maxValTemp.div(4)))}" ,"$maxValTemp")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "color_temp")
                actionsName.add(action)
            }
        }

        if(device.actions?.color != null){
            actions = arrayListOf(context.getString(R.string.create_scene_actions_adapter_choose_color))
            values = arrayListOf("#FF0000")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "color")
                actionsName.add(action)
            }
        }

        return actionsName
    }

    private fun getSectionTypeViaElementPosition(position: Int) : Device? {
        for (i in (detailSceneActions!!.size - 1) downTo 0 step 1) {
            if(detailSceneActions!![i].friendly_name != null) {
                if(i <= position) {
                    return detailSceneActions!![i]
                }
            }
        }
        return null
    }

    private fun getAlreadySelectedActionTypesForDevice(position: Int) : List<Device> {
        var listActions : ArrayList<Device> = ArrayList()
        for (i in position downTo 0 step 1) {
            if(detailSceneActions!![i].friendly_name == null) {
                listActions.add(detailSceneActions!![i])
            } else {
                return listActions
            }
        }
        return listActions
    }
}
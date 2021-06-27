package view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.Actions
import core.rest.model.SceneActionsName
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.HubAccessoryType
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

    var detailSceneActions: ArrayList<HubAccessoryConfiguration>? = null
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
            val actionsName = parseDeviceActionToGetName(device = section!!)
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

        fun bind(action : HubAccessoryConfiguration, position : Int, usedActionTypes : List<HubAccessoryConfiguration>, actionsName : ArrayList<SceneActionsName>?, listener : OnActionClicked, longClicked: OnActionLongClicked) {



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
                    actionTv.text = if(hex.isNotEmpty()) hex[0].key else ""
                } else if (action.actions?.color_temp?.current_state != null) {
                    val temperatureTypes = actionsName!!.filter { elem -> elem.type == "color_temp"}
                    val temperature = temperatureTypes!!.filter { elem -> action.actions!!.color_temp!!.current_state == elem.value.toInt() }
                    actionTv.text = if(temperature.isNotEmpty()) temperature[0].key else ""
                } else {
                    actionTv.text = "Error"
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
                if(action.actions == null && action.friendly_name == null) {
                    val alertDialog: AlertDialog = itemView.context.let {
                        val builder = AlertDialog.Builder(it)
                        builder.apply {
                            this.setTitle(R.string.create_scene_list_action_description)
                            setAdapter(arrayAdapter) {_, which ->
                                listener.actionClicked(actionsName[which], position = position)
                            }
                        }
                        builder.create()
                    }
                    alertDialog.show()
                }
            }

            itemView.setOnLongClickListener {
                if(actionTv.text != itemView.context.getString(R.string.create_scene_add_action)) {
                    AlertDialog.Builder(itemView.context)
                            .setTitle("Supprimer l'action")
                            .setMessage("Êtes-vous sûr de vouloir supprimer cet action ?")
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

        fun bind(action : HubAccessoryConfiguration, position: Int, itemLongClickListener: OnActionLongClicked) {
            actionTv.text = action.name

            itemView.setOnLongClickListener {
                AlertDialog.Builder(itemView.context)
                        .setTitle("Supprimer l'objet")
                        .setMessage("Êtes-vous sûr de vouloir supprimer cet objet de la scène actuelle ?")
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

    private fun parseDeviceActionToGetName(device: HubAccessoryConfiguration) : ArrayList<SceneActionsName> {
        var actions: ArrayList<String>
        var values: ArrayList<String>
        var actionsName = ArrayList<SceneActionsName>()
        if (device.actions?.state != null) {
            actions = arrayListOf("Allumer l'appareil", "Éteindre l'appareil", "Basculer")
            values = arrayListOf("on", "off", "toggle")

            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "state")
                actionsName.add(action)
            }
        }

        if(device.actions?.brightness != null){
            actions = arrayListOf("Régler la luminosité à 25%", "Régler la luminosité à 50%", "Régler la luminosité à 100%")
            values = arrayListOf("25", "50", "100")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "brightness")
                actionsName.add(action)
            }
        }

        if(device.actions?.color != null){
            actions = arrayListOf("Choisir une couleur")
            values = arrayListOf("#FF0000")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "color")
                actionsName.add(action)
            }
        }

        if(device.actions?.color_temp != null){
            actions = arrayListOf("Choisir la température")
            values = arrayListOf("200")
            for (index in actions.indices) {
                val action = SceneActionsName(key = actions[index], value = values[index], type = "color_temp")
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

    private fun getAlreadySelectedActionTypesForDevice(position: Int) : List<HubAccessoryConfiguration> {
        var listActions : ArrayList<HubAccessoryConfiguration> = ArrayList()
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
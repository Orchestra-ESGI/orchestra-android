package view.adapter

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.hubConfiguration.Device
import core.rest.model.hubConfiguration.HubAccessoryType
import utils.OnDeviceListener
import view.ui.DetailDeviceActivity
import viewModel.HomeViewModel


class DeviceAdapter(listener : OnDeviceListener) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>(){

    var deviceList: List<Device>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var layoutInflater: LayoutInflater
    private var itemListener = listener

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val objectIcon = itemView.findViewById<ImageView>(R.id.cell_object_icon_iv)
        private val objectTitle = itemView.findViewById<TextView>(R.id.cell_object_name_tv)
        private val objectRoom = itemView.findViewById<TextView>(R.id.cell_object_room_tv)
        private val objectStatus = itemView.findViewById<TextView>(R.id.cell_object_stat_tv)

        fun bind(device: Device, listener: OnDeviceListener) {
            when(device.type) {
                HubAccessoryType.lightbulb -> objectIcon.setImageResource(R.drawable.ic_lightbulb)
                HubAccessoryType.switch -> objectIcon.setImageResource(R.drawable.ic_switch)
                HubAccessoryType.sensor -> objectIcon.setImageResource(R.drawable.ic_sensor)
                HubAccessoryType.occupancy -> objectIcon.setImageResource(R.drawable.ic_sensor)
                HubAccessoryType.contact -> objectIcon.setImageResource(R.drawable.ic_contact)
                HubAccessoryType.programmableswitch -> objectIcon.setImageResource(R.drawable.ic_programmable_switch)
                HubAccessoryType.temperatureandhumidity -> objectIcon.setImageResource(R.drawable.ic_temperature)
                else -> objectIcon.setImageResource(R.drawable.ic_unknown)

            }
            objectTitle.text = device.name
            objectRoom.text = device.room?.name
            if (device.is_reachable == true) {
                    objectStatus.text = itemView.context.getString(R.string.reachable_ok)
            } else {
                objectStatus.text = itemView.context.getString(R.string.reachable_nok)
            }

            val unwrappedDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.scene_list_item_shape)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            val deviceBackgroundColor = Color.parseColor(device.background_color)
            DrawableCompat.setTint(wrappedDrawable, deviceBackgroundColor)

            itemView.background = unwrappedDrawable

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailDeviceActivity::class.java)
                intent.putExtra("AccessoryDetail", device)
                itemView.context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle(R.string.home_device_delete_title)
                builder.setMessage(R.string.home_device_delete_message)
                builder.setPositiveButton(R.string.home_device_delete_button) { dialog, which ->
                    val friendlyNameToDelete : HashMap<String, List<String>> = HashMap()
                    friendlyNameToDelete["friendly_names"] = listOf(device.friendly_name!!)
                    listener.onLongPressToDeleteDevice(friendlyNameToDelete)
                }
                builder.show()
                true
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_object, parent, false)
        return DeviceViewHolder(objectView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(device = deviceList!![position], listener = itemListener)

    }

    override fun getItemCount(): Int {
        if (deviceList == null) {
            return 0
        }
        return deviceList!!.size
    }
}
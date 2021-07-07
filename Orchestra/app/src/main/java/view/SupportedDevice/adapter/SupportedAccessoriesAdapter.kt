package view.SupportedDevice.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.SupportedDevices
import core.rest.model.device.Device
import view.SupportedDevice.ui.SupportedDeviceListActivity

class SupportedAccessoriesAdapter :
    RecyclerView.Adapter<SupportedAccessoriesAdapter.SupportedDeviceTypeViewHolder>() {

    var supportedDevicesList: List<SupportedDevices>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var device: Device? = null
        set(value) {
        field = value
            notifyDataSetChanged()
        }

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportedDeviceTypeViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_supported_accessory, parent, false)
        return SupportedDeviceTypeViewHolder(objectView)
    }

    class SupportedDeviceTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val supportedDeviceTitle = itemView.findViewById<TextView>(R.id.cell_supported_device_type_tv)

        fun bind(supportedDevices : SupportedDevices, device : Device?) {
            supportedDeviceTitle.text = supportedDevices.brand

            itemView.setOnClickListener {
                if(supportedDevices.devices.size > 0) {
                    val intent = Intent(itemView.context, SupportedDeviceListActivity::class.java)
                    intent.putExtra("SupportedDevice", supportedDevices.devices)
                    intent.putExtra("device", device)
                    intent.putExtra("brand", supportedDevices.brand)
                    itemView.context.startActivity(intent)
                } else {
                    Toast.makeText(itemView.context, itemView.context.getString(R.string.supported_accessories_not_supported_for_this_brand), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onBindViewHolder(holderType: SupportedDeviceTypeViewHolder, position: Int) {
        holderType.bind(supportedDevices = supportedDevicesList!![position], device = device)
    }

    override fun getItemCount(): Int {
        if (supportedDevicesList == null) {
            return 0
        }
        return supportedDevicesList!!.size
    }
}
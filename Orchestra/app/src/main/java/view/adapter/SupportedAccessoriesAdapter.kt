package view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.SupportedAccessories
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import view.ui.SupportedDeviceListActivity

class SupportedAccessoriesAdapter :
    RecyclerView.Adapter<SupportedAccessoriesAdapter.SupportedDeviceTypeViewHolder>() {

    var supportedAccessoriesList: List<SupportedAccessories>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var device: HubAccessoryConfiguration? = null
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

        fun bind(supportedAccessories : SupportedAccessories, device : HubAccessoryConfiguration?) {
            supportedDeviceTitle.text = supportedAccessories.brand

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, SupportedDeviceListActivity::class.java)
                intent.putExtra("SupportedDevice", supportedAccessories.devices)
                intent.putExtra("device", device)
                intent.putExtra("brand", supportedAccessories.brand)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holderType: SupportedDeviceTypeViewHolder, position: Int) {
        holderType.bind(supportedAccessories = supportedAccessoriesList!![position], device = device)
    }

    override fun getItemCount(): Int {
        if (supportedAccessoriesList == null) {
            return 0
        }
        return supportedAccessoriesList!!.size
    }
}
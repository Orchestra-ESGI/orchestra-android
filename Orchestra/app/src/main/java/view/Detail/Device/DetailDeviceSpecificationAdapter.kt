package view.Detail.Device

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import java.util.*
import kotlin.collections.HashMap

class DetailDeviceSpecificationAdapter: RecyclerView.Adapter<DetailDeviceSpecificationAdapter.DetailDeviceSpecificationViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater
    var deviceSpecificationList: HashMap<String, String>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var deviceSpecificationInfoNames: ArrayList<String>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class DetailDeviceSpecificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val specTitle = itemView.findViewById<TextView>(R.id.cell_detail_device_title_tv)
        private val spec = itemView.findViewById<TextView>(R.id.cell_detail_device_tv)

        fun bind(deviceSpecificationTitle: String, deviceSpecification : String) {
            specTitle.text = deviceSpecificationTitle
            spec.text = deviceSpecification

            itemView.setBackgroundResource(R.drawable.detail_device_spec_shape)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailDeviceSpecificationViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val detailDeviceSpecificationView = layoutInflater.inflate(R.layout.cell_detail_device, parent, false)
        return DetailDeviceSpecificationViewHolder(detailDeviceSpecificationView)
    }

    override fun onBindViewHolder(holder: DetailDeviceSpecificationViewHolder, position: Int) {
        var specification = deviceSpecificationInfoNames!![position]
        holder.bind(deviceSpecificationTitle = specification, deviceSpecification = deviceSpecificationList!![specification]!!)
    }

    override fun getItemCount(): Int {
        if (deviceSpecificationList == null || deviceSpecificationInfoNames == null) {
            return 0
        }
        return deviceSpecificationList!!.size
    }
}
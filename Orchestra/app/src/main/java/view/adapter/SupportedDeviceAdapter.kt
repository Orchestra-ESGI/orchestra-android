package view.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orchestra.R
import core.rest.model.ActionScene
import core.rest.model.SupportedDeviceInformations
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import view.ui.CreateDeviceActivity
import view.ui.HomeActivity
import viewModel.DeviceViewModel
import viewModel.HomeViewModel


class SupportedDeviceAdapter : RecyclerView.Adapter<SupportedDeviceAdapter.SupportedDeviceViewHolder>(){
    var supportedDeviceList: List<SupportedDeviceInformations>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var device: HubAccessoryConfiguration? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var brand: String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var deviceVM: DeviceViewModel? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportedDeviceViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_supported_device, parent, false)
        return SupportedDeviceViewHolder(objectView)
    }

    class SupportedDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val supportedDeviceTitle = itemView.findViewById<TextView>(R.id.cell_supported_device_tv)
        private val supportedDeviceImage = itemView.findViewById<ImageView>(R.id.cell_supported_device_iv)

        fun bind(supportedDevice: SupportedDeviceInformations, device : HubAccessoryConfiguration?, brand : String?, deviceVM: DeviceViewModel?) {
            supportedDeviceTitle.text = supportedDevice.name

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)

            Glide.with(itemView).load(supportedDevice.image).apply(options).into(supportedDeviceImage)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CreateDeviceActivity::class.java)
                intent.putExtra("SupportedDevice", supportedDevice)
                intent.putExtra("device", device)
                intent.putExtra("brand", brand)
                if(device == null) {
                    if(supportedDevice.documentation == null) {
                        val builder = AlertDialog.Builder(itemView.context)
                        builder.setTitle("Attention")
                        builder.setMessage("Vous devez réinitialiser votre appareil. Veuillez le réinitialiser")
                        builder.setPositiveButton("Réinitialiser") { dialog, which ->
                            deviceVM!!.resetDevice()
                            itemView.context.startActivity(Intent(itemView.context, HomeActivity::class.java))
                        }
                        builder.show()
                    }
                } else {
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onBindViewHolder(holderType: SupportedDeviceViewHolder, position: Int) {
        holderType.bind(supportedDevice = supportedDeviceList!![position], device = device, brand = brand, deviceVM = deviceVM)
    }

    override fun getItemCount(): Int {
        if (supportedDeviceList == null) {
            return 0
        }
        return supportedDeviceList!!.size
    }
}
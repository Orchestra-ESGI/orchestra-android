package view.adapter

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.Automation
import core.rest.model.hubConfiguration.Device
import utils.OnSceneListener
import view.ui.DetailSceneActivity
import java.io.Serializable

class AutomationAdapter(clickListener: OnSceneListener) : RecyclerView.Adapter<AutomationAdapter.AutomationViewHolder>() {

    var automationList: List<Automation>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var deviceList: List<Device>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val itemListener: OnSceneListener = clickListener
    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutomationViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val automationView = layoutInflater.inflate(R.layout.cell_scene, parent, false)
        return AutomationViewHolder(automationView)
    }

    override fun onBindViewHolder(holder: AutomationViewHolder, position: Int) {
        holder.bind(automation = automationList!![position], deviceList = deviceList!!, listener = itemListener)
    }

    override fun getItemCount(): Int {
        if (automationList == null) {
            return 0
        }
        return automationList!!.size
    }

    class AutomationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val automationIcon = itemView.findViewById<ImageView>(R.id.cell_scene_logo_iv)
        private val automationTitle = itemView.findViewById<TextView>(R.id.cell_scene_title_tv)
        private val automationInfo = itemView.findViewById<ImageView>(R.id.cell_scene_info_iv)
        fun bind(automation: Automation, deviceList: List<Device>, listener: OnSceneListener) {
            automationTitle.text = automation.name

            val unwrappedDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.scene_list_item_shape)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            val sceneBackgroundColor = Color.parseColor(automation.color)
            DrawableCompat.setTint(wrappedDrawable, sceneBackgroundColor)

            itemView.background = unwrappedDrawable

            itemView.setOnClickListener {
                listener.onClickToLaunch(automation._id!!, "automation")
            }

            automationInfo.setOnClickListener {
                val detailAutomationIntent = Intent(itemView.context, DetailSceneActivity::class.java)
                detailAutomationIntent.putExtra("DetailAutomation", automation)
                val args = Bundle()
                args.putSerializable("ARRAYLIST", deviceList as Serializable)
                detailAutomationIntent.putExtra("BUNDLE", args)
                itemView.context.startActivity(detailAutomationIntent)
            }

            itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle(itemView.context.getString(R.string.home_scene_delete_title))
                builder.setMessage(itemView.context.getString(R.string.home_scene_delete_message))
                builder.setPositiveButton(itemView.context.getString(R.string.home_scene_delete_button)) { dialog, which ->
                    listener.onLongPressToDelete(automation._id!!, "automation")
                }
                builder.show()
                true
            }
        }
    }
}
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
import core.rest.model.hubConfiguration.HubAccessoryConfiguration
import core.rest.model.hubConfiguration.HubAccessoryType
import view.ui.DetailSceneActivity
import java.io.Serializable

class AutomationAdapter : RecyclerView.Adapter<AutomationAdapter.AutomationViewHolder>() {

    var automationList: List<Automation>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var deviceList: List<HubAccessoryConfiguration>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutomationViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val automationView = layoutInflater.inflate(R.layout.cell_scene, parent, false)
        return AutomationViewHolder(automationView)
    }

    override fun onBindViewHolder(holder: AutomationViewHolder, position: Int) {
        holder.bind(automation = automationList!![position], deviceList = deviceList!!)
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
        fun bind(automation: Automation, deviceList: List<HubAccessoryConfiguration>) {
            automationTitle.text = automation.name

            val unwrappedDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.scene_list_item_shape)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)

            automation.color.let {
                val sceneBackgroundColor = if(it.first() == '#') {
                    Color.parseColor(it)
                } else {
                    it.toInt()
                }
                DrawableCompat.setTint(wrappedDrawable, sceneBackgroundColor)
            }

            itemView.background = unwrappedDrawable

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
                    /*
                    if(homeVM != null) {
                        homeVM!!.deleteScenes(ListSceneToDelete(listOf(scene._id!!)))
                    }
                     */
                }
                builder.show()
                true
            }
        }
    }
}
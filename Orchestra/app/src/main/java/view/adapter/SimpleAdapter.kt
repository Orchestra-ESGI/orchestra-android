package view.adapter

import android.content.Context
import android.content.Intent
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
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.Scene
import utils.OnActionClicked
import utils.OnItemClicked
import view.ui.DetailSceneActivity

class SimpleAdapter(context: Context, onActionClicked: OnActionClicked) : RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>() {

    private var mContext: Context = context
    var mData: ArrayList<String> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val itemListener: OnActionClicked = onActionClicked

    fun add(s: String, position: Int) {
        var position = position
        position = if (position == -1) itemCount else position
        mData.add(position, s)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        if (position < itemCount) {
            mData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sceneIcon = itemView.findViewById<ImageView>(R.id.cell_scene_logo_iv)
        private val sceneTitle = itemView.findViewById<TextView>(R.id.cell_scene_title_tv)
        fun bind(scene : String, position: Int, mContext : Context, listener: OnActionClicked) {

            sceneTitle.text = scene
            val arrayAdapter = ArrayAdapter<String>(mContext, android.R.layout.select_dialog_item)
            arrayAdapter.add("Allumer l'appareil")
            arrayAdapter.add("Eteindre l'appareil")
            arrayAdapter.add("Luminosité à 25%")
            arrayAdapter.add("Luminosité à 50%")
            arrayAdapter.add("Luminosité à 75%")
            arrayAdapter.add("Luminosité à 100%")

            itemView.setOnClickListener {
                Toast.makeText(mContext, "Position = $scene - ${position}", Toast.LENGTH_SHORT).show()
                val alertDialog: AlertDialog = mContext.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        this.setTitle(R.string.create_scene_list_action_description)
                                .setAdapter(arrayAdapter) { _, which ->
                                    val strName = arrayAdapter.getItem(which)
                                    // listener.actionClicked(strName!!, position)
                                }
                    }
                    builder.create()
                }
                alertDialog.show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val detailDeviceSpecificationView = layoutInflater.inflate(R.layout.cell_scene, parent, false)
        return SimpleViewHolder(detailDeviceSpecificationView)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.bind(mData!![position], position, mContext, itemListener)
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }
}
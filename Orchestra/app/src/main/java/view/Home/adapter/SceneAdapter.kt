package view.Home.adapter

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
import core.rest.model.Scene
import core.rest.model.device.Device
import utils.OnSceneListener
import view.Detail.Scene.DetailSceneActivity
import java.io.Serializable

class SceneAdapter(listener: OnSceneListener) : RecyclerView.Adapter<SceneAdapter.SceneViewHolder>(){

    var sceneList: List<Scene>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var deviceList: List<Device>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var layoutInflater: LayoutInflater

    private var itemListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_scene, parent, false)
        return SceneViewHolder(objectView)
    }

    class SceneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sceneTitle = itemView.findViewById<TextView>(R.id.cell_scene_title_tv)
        private val sceneInfo = itemView.findViewById<ImageView>(R.id.cell_scene_info_iv)
        fun bind(scene : Scene, listener: OnSceneListener, deviceList : List<Device>?) {
            sceneTitle.text = scene.name

            val unwrappedDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.scene_list_item_shape)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)

            scene.color?.let {
                val sceneBackgroundColor = if(it.first() == '#') {
                    Color.parseColor(it)
                } else {
                    it.toInt()
                }
                DrawableCompat.setTint(wrappedDrawable, sceneBackgroundColor)
            }

            itemView.background = unwrappedDrawable

            itemView.setOnClickListener {
                listener.onClickToLaunch(scene._id!!, "scene")
            }

            sceneInfo.setOnClickListener {
                val detailSceneIntent = Intent(itemView.context, DetailSceneActivity::class.java)
                detailSceneIntent.putExtra("DetailScene", scene)
                val args = Bundle()
                args.putSerializable("ARRAYLIST", deviceList as Serializable)
                detailSceneIntent.putExtra("BUNDLE", args)
                itemView.context.startActivity(detailSceneIntent)
            }

            itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle(itemView.context.getString(R.string.home_scene_delete_title))
                builder.setMessage(itemView.context.getString(R.string.home_scene_delete_message))
                builder.setPositiveButton(itemView.context.getString(R.string.home_scene_delete_button)) { dialog, which ->
                    listener.onLongPressToDelete(scene._id!!, "scene")
                }
                builder.show()
                true
            }
        }
    }

    override fun onBindViewHolder(holder: SceneViewHolder, position: Int) {
        holder.bind(scene = sceneList!![position], listener = itemListener, deviceList = deviceList)
    }

    override fun getItemCount(): Int {
        if (sceneList == null) {
            return 0
        }
        return sceneList!!.size
    }

}
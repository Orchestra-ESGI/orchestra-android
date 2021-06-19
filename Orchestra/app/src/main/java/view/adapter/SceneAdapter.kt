package view.adapter

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.ListSceneToDelete
import core.rest.model.Scene
import view.ui.DetailSceneActivity
import viewModel.HomeViewModel

class SceneAdapter : RecyclerView.Adapter<SceneAdapter.SceneViewHolder>(){

    var sceneList: List<Scene>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var homeVM: HomeViewModel? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_scene, parent, false)
        return SceneViewHolder(objectView)
    }

    class SceneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sceneTitle = itemView.findViewById<TextView>(R.id.cell_scene_title_tv)
        fun bind(scene : Scene, homeVM : HomeViewModel?) {
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

            itemView.setBackgroundResource(R.drawable.scene_list_item_shape)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailSceneActivity::class.java)
                intent.putExtra("DetailScene", scene)
                itemView.context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Suppression de la scène ")
                builder.setMessage("Êtes-vous sûr de vouloir supprimer la scène ?")
                builder.setPositiveButton("Supprimer") { dialog, which ->
                    if(homeVM != null) {
                        homeVM!!.deleteScenes(ListSceneToDelete(listOf(scene._id)))
                    }
                }
                builder.show()
                true
            }
        }
    }

    override fun onBindViewHolder(holder: SceneViewHolder, position: Int) {
        holder.bind(scene = sceneList!![position], homeVM = homeVM)
    }

    override fun getItemCount(): Int {
        if (sceneList == null) {
            return 0
        }
        return sceneList!!.size
    }

}
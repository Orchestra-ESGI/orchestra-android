package view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import core.rest.model.ActionScene
import java.util.ArrayList

class DetailSceneActionsAdapter: RecyclerView.Adapter<DetailSceneActionsAdapter.DetailSceneActionsViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater

    var detailSceneActions: List<ActionScene>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class DetailSceneActionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionTv = itemView.findViewById<TextView>(R.id.cell_scene_action_name)

        fun bind(action : ActionScene) {
            actionTv.text = action.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailSceneActionsViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val detailSceneActionView = layoutInflater.inflate(R.layout.cell_detail_scene_action, parent, false)
        return DetailSceneActionsViewHolder(
            detailSceneActionView
        )
    }

    override fun onBindViewHolder(holder: DetailSceneActionsViewHolder, position: Int) {
        holder.bind(action = detailSceneActions!![position])
    }

    override fun getItemCount(): Int {
        if (detailSceneActions == null) {
            return 0
        }
        return detailSceneActions!!.size
    }
}
package view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import utils.OnItemClicked


class ShuffleColorAdapter(clickListener: OnItemClicked): RecyclerView.Adapter<ShuffleColorAdapter.ShuffleColorViewHolder>(){

    var colorListMap: MutableMap<Int, Boolean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var colorList: List<Int>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val itemListener: OnItemClicked = clickListener

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShuffleColorViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_color, parent, false)
        return ShuffleColorViewHolder(objectView)
    }

    override fun onBindViewHolder(holder: ShuffleColorViewHolder, position: Int) {
        var color = colorList!![position]
        holder.bind(color = color, selected = colorListMap!![color]!!, position = position, listener = itemListener)
    }

    override fun getItemCount(): Int {
        if (colorList!!.isEmpty() && colorListMap!!.isEmpty()) {
            return 0
        }
        return colorList!!.size
    }

    class ShuffleColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val colorIcon = itemView.findViewById<ImageView>(R.id.cell_color_iv)

        fun bind(color: Int, selected: Boolean, position: Int, listener: OnItemClicked) {

            if (!selected) {
                colorIcon.setImageResource(0)
            }

            val unwrappedDrawable = AppCompatResources.getDrawable(
                itemView.context,
                R.drawable.create_scene_shuffle_color_shape
            )
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, color)

            itemView.setBackgroundResource(R.drawable.create_scene_shuffle_color_shape)

            itemView.setOnClickListener {
                colorIcon.setImageResource(R.drawable.ic_circle_point)
                listener.colorClicked(color, position)
            }
        }
    }


}
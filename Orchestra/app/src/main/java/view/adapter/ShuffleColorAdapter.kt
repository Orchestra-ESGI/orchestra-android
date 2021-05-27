package view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.orchestra.R


class ShuffleColorAdapter : RecyclerView.Adapter<ShuffleColorAdapter.ShuffleColorViewHolder>(){

    var colorList: List<Int>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var positionSelected = 0

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShuffleColorViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_color, parent, false)
        return ShuffleColorViewHolder(objectView)
    }

    class ShuffleColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val colorIcon = itemView.findViewById<ImageView>(R.id.cell_color_iv)

        fun bind(color: Int, position: Int) {

            if (position != 0) {
                colorIcon.setImageResource(0)
            }

            val unwrappedDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.create_scene_shuffle_color_shape)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, color)

            itemView.setBackgroundResource(R.drawable.create_scene_shuffle_color_shape)

            itemView.setOnClickListener {
                colorIcon.setImageResource(R.drawable.ic_circle_point)
            }
        }
    }

    override fun onBindViewHolder(holder: ShuffleColorViewHolder, position: Int) {
        holder.bind(color = colorList!![position], position = position)
    }

    override fun getItemCount(): Int {
        if (colorList!!.isEmpty()) {
            return 0
        }
        return colorList!!.size
    }

    fun getPosition(): Int {
        return positionSelected
    }
    fun setPosition(position: Int) {
        positionSelected = position
    }
}
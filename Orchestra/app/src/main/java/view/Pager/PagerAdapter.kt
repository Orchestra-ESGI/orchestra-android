package view.Pager

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import view.ui.LoginActivity

class PagerAdapter(var context: Context) : RecyclerView.Adapter<PagerAdapter.PagerViewHolder>() {

    var titleList = listOf(
        context.getString(R.string.pager_slider1_title),
        context.getString(R.string.pager_slider2_title),
        context.getString(R.string.pager_slider3_title),
        context.getString(R.string.pager_slider4_title),
        context.getString(R.string.pager_slider5_title)
    )
    var descriptionList = listOf(
            context.getString(R.string.pager_slider1_text),
            context.getString(R.string.pager_slider2_text),
            context.getString(R.string.pager_slider3_text),
            context.getString(R.string.pager_slider4_text),
            context.getString(R.string.pager_slider5_text)
    )


    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var pagerImageView = itemView.findViewById<ImageView>(R.id.pager_content_image_iv)
        private var pagerTitle = itemView.findViewById<TextView>(R.id.pager_content_title_tv)
        private var pagerDescription = itemView.findViewById<TextView>(R.id.pager_content_description_tv)
        private var pagerContinueButton = itemView.findViewById<Button>(R.id.pager_content_next_button)

        fun bind(context: Context, title: String, description: String) {
            var wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            var display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            pagerImageView.layoutParams.height = (size.y / 2)
            pagerTitle.text = title
            pagerDescription.text = description

            pagerContinueButton.setOnClickListener {
                val loginIntent = Intent(context, LoginActivity::class.java)
                itemView.context.startActivity(loginIntent)
            }
        }

        fun handleShowNextButton(isVisible : Boolean) {
            pagerContinueButton.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(context).inflate(R.layout.pager_content, parent, false))
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(context, titleList[position], descriptionList[position])
        if (position == titleList.size-1) holder.handleShowNextButton(true) else holder.handleShowNextButton(false)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }
}
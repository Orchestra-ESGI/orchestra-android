package view.adapter

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.media.Image
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import view.ui.LoginActivity
import view.ui.SupportedAccessoriesListActivity

class PagerAdapter(var context: Context) : RecyclerView.Adapter<PagerAdapter.PagerViewHolder>() {

    var titleList = listOf(
        "Simplicité",
        "Efficacité",
        "Clarté",
        "Sychronisation",
        "C'est parti"
    )
    var descriptionList = listOf(
        "Gérez votre ou vos domicile & vos objets connectés le tout depuis la même application et simplifiez-vous la vie.",
        "Gérez tout votre docmilice depuis Orchestra simplement et rapidement.",
        "Trop de fils ? Trop de Hub ? Trop d'applications ? Trop de chose pour gérer  votre domotique, avec Orchestra, débarassez vous du surplus, et gardez uniquement l'essentiel.",
        "Vous décidez de changez de téléphone ou de marque? Pas d'inquiétude, réinstallez Orchestra sur votre nouvel appareil, on se charge de tout synchroniser pour vous.",
        "Orchestra, la domotique facile et utile."
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
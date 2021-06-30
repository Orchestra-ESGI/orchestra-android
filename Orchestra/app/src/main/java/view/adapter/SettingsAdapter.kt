package view.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.orchestra.R
import view.ui.LoginActivity
import view.ui.WebViewActivity
import viewModel.UserViewModel

class SettingsAdapter : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    private var titleList = listOf(
            "Rate Us",
            "Share the app",
            "",
            "About Us",
            "Contact",
            "",
            "Librairies we use",
            "",
            "Privacy policy",
            "Terms of use",
            "",
            "Sign out",
            "Delete your account"
    )
    private var descriptionList = listOf(
            R.drawable.ic_star,
            R.drawable.ic_share,
            0,
            R.drawable.ic_group,
            R.drawable.ic_mail,
            0,
            R.drawable.ic_library,
            0,
            R.drawable.ic_privacy_policy,
            R.drawable.ic_terms,
            0,
            R.drawable.ic_logout,
            R.drawable.ic_delete
    )

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_settings, parent, false)
        return SettingsViewHolder(objectView)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(titleList[position], descriptionList[position])
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    class SettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var settingsRecyclerView = itemView.findViewById<RelativeLayout>(R.id.cell_settings_relative_layout)
        private var settingsIconImageView = itemView.findViewById<ImageView>(R.id.cell_settings_icon_iv)
        private var settingsTextView = itemView.findViewById<TextView>(R.id.cell_settings_text_tv)
        private var settingsNextImageView = itemView.findViewById<ImageView>(R.id.cell_settings_next_iv)



        fun bind(title: String, icon: Int) {
            settingsTextView.text = title
            settingsIconImageView.setImageResource(icon)

            if (title == "" && icon == 0) {
                settingsRecyclerView.setBackgroundColor(Color.TRANSPARENT)
                settingsTextView.text = ""
                settingsIconImageView.setImageResource(0)
                settingsNextImageView.setImageResource(0)
            }

            if (title == "Delete your account") {
                // val appRedColor = ContextCompat.getColor(itemView.context, R.color.app_primary_red)
                settingsTextView.setTextColor(Color.RED)
            }

            itemView.setOnClickListener {
                when (title) {
                    "Rate Us" -> rateApp()
                    "Share the app" -> shareApp()
                    "Sign out" -> signOut()
                    "Delete your account" -> deleteAccount()
                    else -> otherViews()
                }
            }
        }

        private fun rateApp() {
            val packageName = itemView.context.packageName
            val uri = Uri.parse("market://details?id=$packageName")
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                itemView.context.startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(itemView.context, "Impossible to find an application for the market", Toast.LENGTH_LONG).show()
            }
        }

        private fun shareApp() {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "Hey Check out this Great app:")
            intent.type = "text/plain"
            itemView.context.startActivity(Intent.createChooser(intent, "Share To:"))
        }

        private fun signOut() {
            AlertDialog.Builder(itemView.context).setTitle("Deconnexion")
                .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setPositiveButton("Deconnexion") { dialog, _ ->
                    val sharedPref = itemView.context.getSharedPreferences("com.example.orchestra.API_TOKEN", Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putString("Token", null)
                        apply()
                    }
                    val intent = Intent(itemView.context, LoginActivity::class.java)
                    itemView.context.startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        private fun otherViews() {
            val intent = Intent(itemView.context, WebViewActivity::class.java)
            itemView.context.startActivity(intent)
        }

        private fun deleteAccount() {
            AlertDialog.Builder(itemView.context)
                .setTitle("Supprimer votre compte")
                .setMessage("Êtes-vous sûr de vouloir supprimer votre compte ?")
                .setPositiveButton("Supprimer") { dialog, _ ->

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}
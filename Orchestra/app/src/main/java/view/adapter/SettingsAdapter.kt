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
import utils.OnSettingListener
import view.ui.LoginActivity
import view.ui.WebViewActivity


class SettingsAdapter(context: Context, clickListener: OnSettingListener) : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    private var titleList = listOf(
        context.getString(R.string.settings_rate_us),
        context.getString(R.string.settings_share_the_app),
        "",
        context.getString(R.string.settings_about_us),
        context.getString(R.string.settings_contact),
        "",
        context.getString(R.string.settings_libraries_we_use),
        "",
        context.getString(R.string.settings_privacy_policy),
        context.getString(R.string.settings_terms_of_use),
        "",
        context.getString(R.string.settings_shutdown),
        context.getString(R.string.settings_reboot),
        "",
        context.getString(R.string.settings_sign_out),
        context.getString(R.string.settings_delete_your_account)
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
        R.drawable.ic_close_24,
        R.drawable.ic_restart_24,
        0,
        R.drawable.ic_logout,
        R.drawable.ic_delete
    )

    private lateinit var layoutInflater: LayoutInflater
    private var listener = clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val objectView = layoutInflater.inflate(R.layout.cell_settings, parent, false)
        return SettingsViewHolder(objectView)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(titleList[position], descriptionList[position], listener)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    class SettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var settingsRecyclerView = itemView.findViewById<RelativeLayout>(R.id.cell_settings_relative_layout)
        private var settingsIconImageView = itemView.findViewById<ImageView>(R.id.cell_settings_icon_iv)
        private var settingsTextView = itemView.findViewById<TextView>(R.id.cell_settings_text_tv)
        private var settingsNextImageView = itemView.findViewById<ImageView>(R.id.cell_settings_next_iv)

        fun bind(title: String, icon: Int, listener: OnSettingListener) {
            settingsTextView.text = title
            settingsIconImageView.setImageResource(icon)

            if (title == "" && icon == 0) {
                settingsRecyclerView.setBackgroundColor(Color.TRANSPARENT)
                settingsTextView.text = ""
                settingsIconImageView.setImageResource(0)
                settingsNextImageView.setImageResource(0)
            }

            if (title == itemView.context.getString(R.string.settings_delete_your_account)) {
                settingsTextView.setTextColor(Color.RED)
            }

            itemView.setOnClickListener {
                when (title) {
                    itemView.context.getString(R.string.settings_rate_us) -> rateApp()
                    itemView.context.getString(R.string.settings_share_the_app) -> shareApp()
                    itemView.context.getString(R.string.settings_about_us) -> otherViews("https://orchestra-website.herokuapp.com/about")
                    itemView.context.getString(R.string.settings_contact) -> sendEmail()
                    itemView.context.getString(R.string.settings_privacy_policy) -> otherViews("https://orchestra-website.herokuapp.com/privacy")
                    itemView.context.getString(R.string.settings_terms_of_use) -> otherViews("https://orchestra-website.herokuapp.com/cgu")
                    itemView.context.getString(R.string.settings_libraries_we_use) -> otherViews("https://kotlinlang.org/")
                    itemView.context.getString(R.string.settings_shutdown) -> shutdownHub(listener)
                    itemView.context.getString(R.string.settings_reboot) -> rebootHub(listener)
                    itemView.context.getString(R.string.settings_sign_out) -> signOut()
                    itemView.context.getString(R.string.settings_delete_your_account) -> deleteAccount(
                        listener
                    )
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
                Toast.makeText(
                    itemView.context,
                    itemView.context.getString(R.string.settings_rate_app_impossible),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        private fun shareApp() {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                itemView.context.getString(R.string.settings_share_app_text)
            )
            intent.type = "text/plain"
            itemView.context.startActivity(
                Intent.createChooser(
                    intent,
                    itemView.context.getString(R.string.settings_share_app_title)
                )
            )
        }

        private fun sendEmail() {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "orchestra.nrv.dev@gmail.com", null))
            itemView.context.startActivity(Intent.createChooser(emailIntent, "Contact"))

        }

        private fun shutdownHub(listener: OnSettingListener) {
            AlertDialog.Builder(itemView.context).setTitle(itemView.context.getString(R.string.settings_shutdown))
                .setMessage(itemView.context.getString(R.string.settings_shutdown_message))
                .setPositiveButton(itemView.context.getString(R.string.settings_shutdown)) { dialog, _ ->
                    listener.onShutdownHub()
                    dialog.dismiss()
                }
                .setNegativeButton(itemView.context.getString(R.string.settings_sign_out_alert_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        private fun rebootHub(listener: OnSettingListener) {
            AlertDialog.Builder(itemView.context).setTitle(itemView.context.getString(R.string.settings_reboot))
                .setMessage(itemView.context.getString(R.string.settings_reboot_message))
                .setPositiveButton(itemView.context.getString(R.string.settings_reboot)) { dialog, _ ->
                    listener.onRebootHub()
                    dialog.dismiss()
                }
                .setNegativeButton(itemView.context.getString(R.string.settings_sign_out_alert_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        private fun signOut() {
            AlertDialog.Builder(itemView.context).setTitle(itemView.context.getString(R.string.settings_sign_out_alert_title))
                .setMessage(itemView.context.getString(R.string.settings_sign_out_alert_message))
                .setPositiveButton(itemView.context.getString(R.string.settings_sign_out_alert_sign_out)) { dialog, _ ->

                    val sharedPref = itemView.context.getSharedPreferences(
                        "com.example.orchestra.API_TOKEN",
                        Context.MODE_PRIVATE
                    )
                    with(sharedPref.edit()) {
                        putString("Token", null)
                        apply()
                    }
                    val intent = Intent(itemView.context, LoginActivity::class.java)
                    itemView.context.startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton(itemView.context.getString(R.string.settings_sign_out_alert_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        private fun otherViews(url: String? = null) {
            val intent = Intent(itemView.context, WebViewActivity::class.java)
            if (url != null) {
                intent.putExtra("URL", url)
            }
            itemView.context.startActivity(intent)
        }

        private fun deleteAccount(listener: OnSettingListener) {
            AlertDialog.Builder(itemView.context)
                .setTitle(itemView.context.getString(R.string.settings_delete_your_account_alert_title))
                .setMessage(itemView.context.getString(R.string.settings_delete_your_account_alert_message))
                .setPositiveButton(itemView.context.getString(R.string.settings_delete_your_account_alert_delete)) { dialog, _ ->
                    listener.onDeleteAccount()
                    itemView.context.startActivity(
                        Intent(
                            itemView.context,
                            LoginActivity::class.java
                        )
                    )
                    dialog.dismiss()
                }
                .setNegativeButton(itemView.context.getString(R.string.settings_delete_your_account_alert_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}
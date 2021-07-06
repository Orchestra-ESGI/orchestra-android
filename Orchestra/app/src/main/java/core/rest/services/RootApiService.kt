package core.rest.services

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.orchestra.R
import core.utils.ApiUtils
import view.ui.LoginActivity

object RootApiService {
    var ROOT_PATH = ApiUtils.getDomain()

    fun handleError(context : Context, code : Int) {
        when(code) {
            400 -> Toast.makeText(context, context.getString(R.string.error_400_BAD_REQUEST), Toast.LENGTH_LONG).show()
            401 -> {
                AlertDialog.Builder(context)
                        .setTitle("Accès refusé")
                        .setMessage("Veuillez vous reconnectez")
                        .setPositiveButton("OK") { dialog, _ ->
                            context.startActivity(Intent(context, LoginActivity::class.java))
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()
            }
            403 -> Toast.makeText(context, context.getString(R.string.error_user), Toast.LENGTH_LONG).show()
            500 -> Toast.makeText(context, context.getString(R.string.error_500_INTERNAL_ERROR), Toast.LENGTH_LONG).show()
            else -> Toast.makeText(context, context.getString(R.string.app_error), Toast.LENGTH_LONG).show()
        }
    }

}
package core.push

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProviders
import com.example.orchestra.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import core.rest.client.UserClient
import core.rest.model.FirebaseToken
import core.rest.services.RootApiService
import core.rest.services.UserServices
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import view.ui.MainActivity


class MyFirebaseService: FirebaseMessagingService() {

    private val NOTIFICATION_ID = R.string.app_name
    private val NOTIFICATION_CHANNEL = "PUSH_NOTIFICATION"

    override fun onMessageReceived(@NonNull remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        showNotification(remoteMessage.notification!!.body!!)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        postNewToken(p0)
    }

    private fun postNewToken(token: String) {
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    val apiToken = getSharedPreferences("com.example.orchestra.API_TOKEN", MODE_PRIVATE).getString("Token", "")
                    builder.header("Authorization", "Bearer $apiToken")
                    builder.header("App-Key", "orchestra")
                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(RootApiService.ROOT_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val client = retrofit.create(UserServices::class.java)

        client.sendFcmToken(FirebaseToken(token)).enqueue(object : Callback<HashMap<String, Any>> {
            override fun onResponse(call: Call<HashMap<String, Any>>, response: Response<HashMap<String, Any>>) {

            }

            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {

            }
        })
    }


    private fun showNotification(msg: String) {

        val activity = Intent(this, MainActivity::class.java)
        activity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val contentIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            activity,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        /*
         * Init the notification builder
         */
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        notificationBuilder.setSmallIcon(R.drawable.ic_orchestra);
        notificationBuilder.setContentTitle(getString(R.string.app_name));
        notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(msg));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setOngoing(false);
        notificationBuilder.setContentText(msg);
        notificationBuilder.setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId("default");
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}
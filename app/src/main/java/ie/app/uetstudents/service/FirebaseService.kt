package ie.app.uetstudents.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.util.SharedPreferencesUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ie.app.uetstudents.MainActivity
import ie.app.uetstudents.R
import ie.app.uetstudents.data.response.FirebaseMessageModel
import ie.app.uetstudents.ui.login.SigninActivity
import ie.app.uetstudents.utils.PreferenceUtils
import org.greenrobot.eventbus.EventBus

class FirebaseService : FirebaseMessagingService() {
    companion object {
        const val KEY_DATA_FCM = "KEY_DATA_FCM"
        const val UPDATE_NOTIFICATION = "UPDATE_NOTIFICATION"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("dLog", "FirebaseService  onNewToken: $token")
        PreferenceUtils.saveString(KEY_DATA_FCM, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        try {
            if (remoteMessage.data.isNotEmpty()) {
                val maps = remoteMessage.data
                val gson = Gson()
                val json = gson.toJson(maps)
                val option = gson.fromJson(json, FirebaseMessageModel::class.java)
                sendNotificationOptions(option)
            } else if (remoteMessage.notification != null) {
                val response = remoteMessage.notification?.body
                sendNotification(response)
            }
            EventBus.getDefault().post(UPDATE_NOTIFICATION)
        } catch (e: Exception) {
            Log.e("Tag", "Messsage: ${e.message}")
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(data: String?) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,345,intent,0)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelID = "UET"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, channelID)
        val drawable = ContextCompat.getDrawable(this, R.drawable.unnamed)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val notification =
            builder.setSmallIcon(R.drawable.unnamed)
                .setLargeIcon(bitmap)
                .setTicker(getString(R.string.app_name))
                .setContentTitle(getString(R.string.app_name)).setStyle(
                    NotificationCompat.BigTextStyle().bigText(data)
                )
                .setAutoCancel(true).setSound(defaultSoundUri).setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setPriority(
                    NotificationManager.IMPORTANCE_HIGH
                ).setContentText(data)
        var notificationChannel: NotificationChannel? =
            notificationManager.getNotificationChannel(channelID)
        if (notificationChannel == null) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            notificationChannel = NotificationChannel(channelID, data, importance)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(System.currentTimeMillis().toInt(), notification.build())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotificationOptions(data: FirebaseMessageModel) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelID = "UET"
        var intent : Intent
        if (PreferenceUtils.getUser() != null)
        {
             intent = Intent(applicationContext,MainActivity::class.java)
        }else
        {
            intent = Intent(applicationContext , SigninActivity::class.java)
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext,100,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, channelID)
        val drawable = ContextCompat.getDrawable(this, R.drawable.unnamed)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val notification = builder.setSmallIcon(R.drawable.unnamed)
            .setLargeIcon(bitmap)
            .setTicker(getString(R.string.app_name))
            .setContentTitle(getString(R.string.app_name))
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(data.title)
            )
            .setAutoCancel(true).setSound(defaultSoundUri).setDefaults(Notification.DEFAULT_ALL)
            .setPriority(
                NotificationManager.IMPORTANCE_HIGH
            ).setContentText(data.message)
        var notificationChannel: NotificationChannel? =
            notificationManager.getNotificationChannel(channelID)
        if (notificationChannel == null) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            notificationChannel =
                NotificationChannel(channelID, "Channel Notification", importance)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(System.currentTimeMillis().toInt(), notification.build())
    }
}
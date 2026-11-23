package tn.poly.cookify.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import tn.poly.cookify.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "cookify_channel"
    }

    fun createChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Cookify Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = "Meal reminders"

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String) {

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        manager.notify(1, builder.build())
    }
}
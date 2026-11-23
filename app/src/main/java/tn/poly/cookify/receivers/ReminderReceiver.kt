package tn.poly.cookify.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import tn.poly.cookify.utils.NotificationHelper

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val helper = NotificationHelper(context)

        helper.showNotification(
            "Cookify Reminder",
            "Don't forget to check your meal plan today!"
        )
    }
}
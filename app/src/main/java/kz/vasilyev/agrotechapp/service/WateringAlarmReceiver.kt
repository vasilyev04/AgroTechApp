package kz.vasilyev.agrotechapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WateringAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val gardenId = intent.getLongExtra("gardenId", -1)
        val gardenTitle = intent.getStringExtra("gardenTitle") ?: return

        if (gardenId != -1L) {
            val notificationService = WateringNotificationService(context)
            notificationService.showNotification(gardenId, gardenTitle)
        }
    }

    companion object {
        const val ACTION_WATERING_ALARM = "kz.vasilyev.agrotechapp.WATERING_ALARM"
    }
} 
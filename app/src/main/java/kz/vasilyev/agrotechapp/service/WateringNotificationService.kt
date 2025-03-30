package kz.vasilyev.agrotechapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import kz.vasilyev.agrotechapp.MainActivity
import kz.vasilyev.agrotechapp.R

class WateringNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val WATERING_CHANNEL_ID = "watering_channel"
        const val WATERING_CHANNEL_NAME = "Напоминания о поливе"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                WATERING_CHANNEL_ID,
                WATERING_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Канал для уведомлений о поливе растений"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(gardenId: Long, gardenTitle: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("gardenId", gardenId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            gardenId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, WATERING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_date)
            .setContentTitle("Время полива!")
            .setContentText("Пора полить ваш сад: $gardenTitle")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(gardenId.toInt(), notification)

        // Перепланируем следующее уведомление
        val garden = kz.vasilyev.agrotechapp.data.RoomInstance
            .getInstance(context.applicationContext as android.app.Application)
            .roomDao()
            .getGarden(gardenId)

        garden?.let {
            kz.vasilyev.agrotechapp.utils.AlarmScheduler.scheduleWateringAlarm(
                context = context,
                gardenId = gardenId,
                gardenTitle = gardenTitle,
                wateringTimeMillis = it.wateringTime
            )
        }
    }
} 
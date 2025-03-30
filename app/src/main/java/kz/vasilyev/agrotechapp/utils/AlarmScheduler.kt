package kz.vasilyev.agrotechapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import kz.vasilyev.agrotechapp.service.WateringAlarmReceiver
import java.util.Calendar

object AlarmScheduler {
    fun scheduleWateringAlarm(context: Context, gardenId: Long, gardenTitle: String, wateringTimeMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, WateringAlarmReceiver::class.java).apply {
            action = WateringAlarmReceiver.ACTION_WATERING_ALARM
            putExtra("gardenId", gardenId)
            putExtra("gardenTitle", gardenTitle)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            gardenId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Получаем часы и минуты из wateringTimeMillis
        val wateringCalendar = Calendar.getInstance().apply {
            timeInMillis = wateringTimeMillis
        }
        val wateringHour = wateringCalendar.get(Calendar.HOUR_OF_DAY)
        val wateringMinute = wateringCalendar.get(Calendar.MINUTE)

        // Устанавливаем время для следующего срабатывания
        val nextAlarm = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, wateringHour)
            set(Calendar.MINUTE, wateringMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Если время уже прошло сегодня, переносим на завтра
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(nextAlarm.timeInMillis, pendingIntent),
                        pendingIntent
                    )
                } else {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        nextAlarm.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextAlarm.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Если нет разрешения на точные алармы, используем неточный alarm
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                nextAlarm.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun cancelWateringAlarm(context: Context, gardenId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WateringAlarmReceiver::class.java).apply {
            action = WateringAlarmReceiver.ACTION_WATERING_ALARM
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            gardenId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
} 
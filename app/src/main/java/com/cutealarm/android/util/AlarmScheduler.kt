package com.cutealarm.android.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.cutealarm.android.data.database.AlarmEntity
import com.cutealarm.android.data.database.RepeatPattern
import com.cutealarm.android.receiver.AlarmReceiver
import java.util.Calendar

object AlarmScheduler {

    private const val ALARM_ACTION = "com.cutealarm.android.ALARM_TRIGGER"

    fun scheduleAlarm(context: Context, alarm: AlarmEntity) {
        if (!alarm.isEnabled) {
            cancelAlarm(context, alarm.id)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = ALARM_ACTION
            putExtra("alarm_id", alarm.id)
            putExtra("alarm_label", alarm.label)
            putExtra("alarm_ringtone", alarm.ringtoneUri)
            putExtra("alarm_vibrate", alarm.isVibrate)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = calculateNextTriggerTime(alarm)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            }
            else -> {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
                    pendingIntent
                )
            }
        }
    }

    fun cancelAlarm(context: Context, alarmId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = ALARM_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun calculateNextTriggerTime(alarm: AlarmEntity): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 如果设置的时间已经过了今天的时刻，则从明天开始计算
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // 根据重复模式调整
        when (alarm.repeatPattern) {
            RepeatPattern.ONCE -> {
                // 单次闹钟，已经设置好了时间
            }
            RepeatPattern.DAILY -> {
                // 每天，已经设置好了时间
            }
            RepeatPattern.WEEKDAYS -> {
                // 工作日 (周一到周五)
                while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                    calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            RepeatPattern.WEEKENDS -> {
                // 周末 (周六和周日)
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            RepeatPattern.CUSTOM -> {
                // 自定义天数
                val days = alarm.repeatDays.split(",")
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() }
                    .toSet()

                if (days.isNotEmpty()) {
                    while (calendar.get(Calendar.DAY_OF_WEEK) !in days) {
                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                    }
                }
            }
        }

        return calendar.timeInMillis
    }

    fun rescheduleAllAlarms(context: Context, alarms: List<AlarmEntity>) {
        alarms.forEach { alarm ->
            if (alarm.isEnabled) {
                scheduleAlarm(context, alarm)
            }
        }
    }
}

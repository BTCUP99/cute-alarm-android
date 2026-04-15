package com.cutealarm.android.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cutealarm.android.CuteAlarmApplication
import com.cutealarm.android.R
import com.cutealarm.android.ui.alarm.AlarmRingActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_ALARM_TRIGGER) return

        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, -1)
        val alarmLabel = intent.getStringExtra(EXTRA_ALARM_LABEL) ?: ""
        val alarmRingtone = intent.getStringExtra(EXTRA_ALARM_RINGTONE) ?: ""
        val alarmVibrate = intent.getBooleanExtra(EXTRA_ALARM_VIBRATE, true)

        // 启动闹钟响铃界面
        val ringIntent = Intent(context, AlarmRingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_ALARM_ID, alarmId)
            putExtra(EXTRA_ALARM_LABEL, alarmLabel)
            putExtra(EXTRA_ALARM_RINGTONE, alarmRingtone)
            putExtra(EXTRA_ALARM_VIBRATE, alarmVibrate)
        }

        // 根据 Android 版本选择启动方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ 使用 fullScreenIntent
            val fullScreenIntent = PendingIntent.getActivity(
                context,
                alarmId.toInt(),
                ringIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CuteAlarmApplication.ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("可爱闹钟")
                .setContentText(alarmLabel.ifEmpty { "闹钟响了!" })
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenIntent, true)
                .setAutoCancel(true)
                .build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(alarmId.toInt(), notification)
        } else {
            context.startActivity(ringIntent)
        }
    }

    companion object {
        const val ACTION_ALARM_TRIGGER = "com.cutealarm.android.ALARM_TRIGGER"
        const val EXTRA_ALARM_ID = "alarm_id"
        const val EXTRA_ALARM_LABEL = "alarm_label"
        const val EXTRA_ALARM_RINGTONE = "alarm_ringtone"
        const val EXTRA_ALARM_VIBRATE = "alarm_vibrate"
    }
}
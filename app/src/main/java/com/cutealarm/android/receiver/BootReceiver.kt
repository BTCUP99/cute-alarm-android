package com.cutealarm.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cutealarm.android.CuteAlarmApplication
import com.cutealarm.android.util.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        // 重新调度所有已启用的闹钟
        val application = context.applicationContext as CuteAlarmApplication
        val alarmDao = application.database.alarmDao()

        CoroutineScope(Dispatchers.IO).launch {
            val enabledAlarms = alarmDao.getEnabledAlarms()
            AlarmScheduler.rescheduleAllAlarms(context, enabledAlarms)
        }
    }
}
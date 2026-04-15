package com.cutealarm.android.data.repository

import com.cutealarm.android.data.database.AlarmDao
import com.cutealarm.android.data.database.AlarmEntity
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao: AlarmDao) {

    val allAlarms: Flow<List<AlarmEntity>> = alarmDao.getAllAlarms()

    suspend fun getAlarmById(id: Long): AlarmEntity? {
        return alarmDao.getAlarmById(id)
    }

    suspend fun getEnabledAlarms(): List<AlarmEntity> {
        return alarmDao.getEnabledAlarms()
    }

    suspend fun insertAlarm(alarm: AlarmEntity): Long {
        return alarmDao.insertAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: AlarmEntity) {
        alarmDao.updateAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: AlarmEntity) {
        alarmDao.deleteAlarm(alarm)
    }

    suspend fun deleteAlarmById(id: Long) {
        alarmDao.deleteAlarmById(id)
    }

    suspend fun setAlarmEnabled(id: Long, enabled: Boolean) {
        alarmDao.setAlarmEnabled(id, enabled)
    }
}

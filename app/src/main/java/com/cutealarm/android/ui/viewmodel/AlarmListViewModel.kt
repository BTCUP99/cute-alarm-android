package com.cutealarm.android.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cutealarm.android.CuteAlarmApplication
import com.cutealarm.android.data.database.AlarmEntity
import com.cutealarm.android.data.database.RepeatPattern
import com.cutealarm.android.data.repository.AlarmRepository
import com.cutealarm.android.util.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlarmListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlarmRepository

    private val _alarms = MutableStateFlow<List<AlarmEntity>>(emptyList())
    val alarms: StateFlow<List<AlarmEntity>> = _alarms.asStateFlow()

    init {
        val alarmDao = (application as CuteAlarmApplication).database.alarmDao()
        repository = AlarmRepository(alarmDao)
        loadAlarms()
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            repository.allAlarms.collect { alarmList ->
                _alarms.value = alarmList
            }
        }
    }

    fun addAlarm(
        hour: Int,
        minute: Int,
        label: String = "",
        repeatPattern: RepeatPattern = RepeatPattern.ONCE,
        repeatDays: String = "",
        isVibrate: Boolean = true,
        ringtoneUri: String = ""
    ) {
        viewModelScope.launch {
            val alarm = AlarmEntity(
                hour = hour,
                minute = minute,
                label = label,
                repeatPattern = repeatPattern,
                repeatDays = repeatDays,
                isVibrate = isVibrate,
                ringtoneUri = ringtoneUri
            )
            val id = repository.insertAlarm(alarm)
            val savedAlarm = alarm.copy(id = id)
            AlarmScheduler.scheduleAlarm(getApplication(), savedAlarm)
        }
    }

    fun updateAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            repository.updateAlarm(alarm)
            AlarmScheduler.scheduleAlarm(getApplication(), alarm)
        }
    }

    fun deleteAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
            AlarmScheduler.cancelAlarm(getApplication(), alarm.id)
        }
    }

    fun toggleAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            val updatedAlarm = alarm.copy(isEnabled = !alarm.isEnabled)
            repository.updateAlarm(updatedAlarm)
            if (updatedAlarm.isEnabled) {
                AlarmScheduler.scheduleAlarm(getApplication(), updatedAlarm)
            } else {
                AlarmScheduler.cancelAlarm(getApplication(), alarm.id)
            }
        }
    }
}

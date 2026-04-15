package com.cutealarm.android.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val label: String = "",
    val isEnabled: Boolean = true,
    val repeatPattern: RepeatPattern = RepeatPattern.ONCE,
    val repeatDays: String = "", // 逗号分隔的天数: "1,2,3,4,5"
    val isVibrate: Boolean = true,
    val ringtoneUri: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

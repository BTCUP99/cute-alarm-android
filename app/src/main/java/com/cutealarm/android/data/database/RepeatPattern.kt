package com.cutealarm.android.data.database

enum class RepeatPattern(val displayName: String, val days: IntArray) {
    ONCE("仅一次", intArrayOf()),
    DAILY("每天", intArrayOf(1, 2, 3, 4, 5, 6, 7)),
    WEEKDAYS("工作日", intArrayOf(2, 3, 4, 5, 6)),
    WEEKENDS("周末", intArrayOf(1, 7)),
    CUSTOM("自定义", intArrayOf());

    companion object {
        fun fromDays(days: IntArray): RepeatPattern {
            return when {
                days.isEmpty() -> ONCE
                days.contentEquals(intArrayOf(1, 2, 3, 4, 5, 6, 7)) -> DAILY
                days.contentEquals(intArrayOf(2, 3, 4, 5, 6)) -> WEEKDAYS
                days.contentEquals(intArrayOf(1, 7)) -> WEEKENDS
                else -> CUSTOM
            }
        }
    }
}

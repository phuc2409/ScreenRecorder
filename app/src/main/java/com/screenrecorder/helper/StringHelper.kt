package com.screenrecorder.helper

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class StringHelper {
    companion object {
        fun getDateTimeNow(): String {
            val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
            val now = Date(System.currentTimeMillis())
            return format.format(now)
        }
    }
}
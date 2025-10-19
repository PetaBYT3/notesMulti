package org.notes.multi.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TimeRepository {

    fun getCurrentDateTime() : String {
        val timeNow = Clock.System.now()
        val localDateTime = timeNow.toLocalDateTime(TimeZone.currentSystemDefault())

        val years = localDateTime.year
        val month = localDateTime.monthNumber
        val date = localDateTime.dayOfMonth

        val hours = localDateTime.hour
        val minutes = localDateTime.minute

        val currentDateTime = "$years-$month-$date $hours:$minutes"

        return currentDateTime
    }
}
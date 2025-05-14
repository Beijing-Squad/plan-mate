package ui.main

import kotlinx.datetime.LocalDateTime

fun LocalDateTime.format(): String {
    val month = monthNumber.toString().padStart(2, '0')
    val day = dayOfMonth.toString().padStart(2, '0')
    val hour = hour.toString().padStart(2, '0')
    val minute = minute.toString().padStart(2, '0')

    return "$year-$month-$day,$hour:$minute"
}
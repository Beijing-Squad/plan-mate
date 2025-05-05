import kotlinx.datetime.LocalDateTime

fun LocalDateTime.format(): String {
    val monthString = monthNumber.toString().padStart(2, '0')
    val dayString = dayOfMonth.toString().padStart(2, '0')
    val hourString = hour.toString().padStart(2, '0')
    val minuteString = minute.toString().padStart(2, '0')

    return "$year-$monthString-$dayString,$hourString:$minuteString"
}
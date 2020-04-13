package kafffe.calendar

data class TimeOfDay(val hour: Int, val minute: Int) {
    fun addMinutes(deltaMinutes: Int) : TimeOfDay {
        val newMinute = minute + deltaMinutes
        val newHour = hour + (newMinute / 60)
        return TimeOfDay(newHour, newMinute % 60)
    }

    operator fun compareTo(timeOfDay: TimeOfDay): Int =
        (minutesAfterDayStart).compareTo(timeOfDay.minutesAfterDayStart)

    val minutesAfterDayStart get() = minute  + hour * 60

    val formatted get() = """${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"""
}


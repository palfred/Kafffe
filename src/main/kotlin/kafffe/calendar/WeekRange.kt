package kafffe.calendar

data class WeekRange(val weekDay: WeekDay, val startTime: TimeOfDay, val durationMinutes: Int) {
    val endTime get() = startTime.addMinutes(durationMinutes)

    fun overlaps(other: WeekRange) =
        weekDay == other.weekDay && (startTime >= other.startTime && startTime <= other.endTime)

}


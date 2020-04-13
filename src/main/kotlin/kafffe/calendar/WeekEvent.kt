package kafffe.calendar

import kotlin.random.Random

data class WeekEvent(
    val id: String,
    var time: WeekRange,
    var title: String,
    var description: String
// TODO eventType
) {

    companion object {
        val demoEvents = (1..50).map {
            val startTime = TimeOfDay(Random.nextInt(8, 15), Random.nextInt(0, 4) * 15)
            val duration = Random.nextInt(1,10) * 10
            WeekEvent(
                it.toString(),
                WeekRange(WeekDay.values()[Random.nextInt(0, 7)],  startTime, duration),
                "Hello ($it)",
                "Description ($it)"
            )
        }
    }
}


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
        val demoEvents = (1..30).map {
            val startTime = TimeOfDay(Random.nextInt(8, 15), Random.nextInt(0, 60))
            val duration = Random.nextInt(30,91)
            val time = WeekRange(WeekDay.values()[Random.nextInt(0, 5)], startTime, duration)
            WeekEvent(
                it.toString(),
                time,
                "Hello ($it)",
                """${startTime.formatted} ${time.weekDay} 
                   |$duration min.""".trimMargin()
            )
        }
    }
}


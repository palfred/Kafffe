package kafffe.calendar

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import kafffe.core.KafffeHtmlOut
import kotlin.browser.window
import kotlin.random.Random

class WeekCalendar2() : KafffeComponent() {

    var startTime: TimeOfDay = TimeOfDay(8, 0)
    var endTime: TimeOfDay = TimeOfDay(16, 0)
    var stepsPerHour: Int = 4 // must be divisor of 60
    private val minutesStep: Int get() = 60 / stepsPerHour

    var days = arrayOf("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag")
    private val times: List<TimeOfDay> =
        generateSequence(startTime, { it.addMinutes(minutesStep) }).takeWhile { it < endTime }.toList()

    private val timesHour: List<TimeOfDay> =
        generateSequence(startTime, { it.addMinutes(60) }).takeWhile { it < endTime }.toList()


    val headerHeight: Int = 30
    val timeWidth: Int = 64

    var totalWidth: Int by rerenderOnChange(window.innerWidth - 30)
    var totalHeight: Int by rerenderOnChange(window.innerHeight - 100)

    val cellHeight: Int get() = (totalHeight - headerHeight) / (times.size)
    val cellWidth: Int get() = (totalWidth - timeWidth) / (days.size)

    var headerColor: String by rerenderOnChange("#343")
    var cellColor: String by rerenderOnChange("#555")
    var textColor: String by rerenderOnChange("#fdf")
    var gridColor: String by rerenderOnChange("#000")


    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut =
        div {
            addClass("wc_container")
            withStyle {
                backgroundColor = cellColor
                color = textColor
                position = "relative"
                width = "${totalWidth}px"
                height = "${totalHeight}px"
                textAlign = "center"
            }

            div {
                addClass("wc_timeline")
                withStyle {
                    position = "absolute"
                    width = "${timeWidth}px"
                    height = "${totalHeight}px"
                    top = "0px"
                    left = "0px"
                    backgroundColor = headerColor
                }

                for ((index, current) in timesHour.withIndex()) {
                    div {
                        addClass("wc_time")
                        withStyle {
                            border = "1px solid $gridColor"
                            position = "absolute"
                            top = "${cellHeight * stepsPerHour * index + headerHeight}px"
                            width = "${timeWidth}px"
                            height = "${cellHeight * stepsPerHour}px"
                        }

                        text(current.formatted)
                    }
                }
            }
            for ((dayIndex, day) in days.withIndex()) {
                div {
                    addClass("wc_day")
                    withStyle {
                        position = "absolute"
                        width = "${cellWidth}px"
                        height = "${totalHeight}px"
                        top = "0px"
                        left = "${dayIndex * cellWidth + timeWidth}px"
                    }
                    div {
                        addClass("wc_dayheader")
                        withStyle {
                            backgroundColor = headerColor
                            border = "1px solid $gridColor"
                            position = "absolute"
                            top = "0px"
                            width = "${cellWidth}px"
                            height = "${headerHeight}px"
                        }
                        text(day)
                    }
                    for ((index, current) in times.withIndex()) {
                        div {
                            addClass("wc_cell")
                            withStyle {
                                border = "1px solid #000"
                                position = "absolute"
                                top = "${cellHeight * index + headerHeight}px"
                                width = "${cellWidth}px"
                                height = "${cellHeight}px"

                            }
                            text(" ")
                        }
                    }
                    // TODO for events in day
                    repeat(Random.nextInt(1, 4)) {
                        div {
                            withStyle {
                                border = "1px solid black"
                                backgroundColor = "#060"
                                color = "#FFF"
                                opacity = "0.8"
                                width = "90%"
                                top = "${cellHeight * Random.nextInt(0, 30) + headerHeight}px"
                                height = "${Random.nextInt(1, 5) * cellHeight}px"
                                position = "absolute"
                                left = "5%"
                                padding = "0.1rem"
                            }
                            text("Hello")
                        }
                    }
                }
            }
        }

}


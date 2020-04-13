package kafffe.calendar

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import kafffe.core.KafffeHtmlOut
import kotlin.browser.window
import kotlin.random.Random

class WeekCalendar() : KafffeComponent() {

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

    val cellHeight: Int by rerenderOnChange(30)
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
                height = "${headerHeight}px"
                textAlign = "center"
            }

            div {
                addClass("wc_days")
                withStyle {
                    position = "absolute"
                    width = "${totalWidth}px"
                    height = "${headerHeight}px"
                    top = "0px"
                    left = "0px"
                    backgroundColor = headerColor
                }
                for ((dayIndex, day) in days.withIndex()) {
                    div {
                        addClass("wc_day")
                        withStyle {
                            position = "absolute"
                            width = "${cellWidth}px"
                            height = "${headerHeight}px"
                            top = "0px"
                            left = "${dayIndex * cellWidth + timeWidth}px"
                        }
                        text(day)
                    }
                }
            }

            div {
                addClass("wc_timecontainer_scroll")
                withStyle {
                    position = "absolute"
                    width = "${totalWidth + 10}px"
                    height = "${totalHeight - headerHeight}px"
                    maxHeight = "${totalHeight - headerHeight}px"
                    top = "${headerHeight}px"
                    left = "0px"
                    overflowY = "auto"
                }
                div {
                    addClass("wc_timecontainer")
                    withStyle {
                        position = "relative"
                        width = "${totalWidth}px"
                        maxHeight = "${times.size * cellHeight}px"
                        backgroundColor = cellColor
                    }

                    // Time column
                    div {
                        addClass("wc_timeline")
                        withStyle {
                            position = "absolute"
                            width = "${timeWidth}px"
                            height = "${times.size * cellHeight}px"
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
                                    top = "${cellHeight * stepsPerHour * index}px"
                                    width = "${timeWidth}px"
                                    height = "${cellHeight * stepsPerHour}px"
                                }

                                text(current.formatted)
                            }
                        }
                    }

                    // day columns
                    for ((dayIndex, day) in days.withIndex()) {
                        div {
                            addClass("wc_day")
                            withStyle {
                                position = "absolute"
                                width = "${cellWidth}px"
                                height = "${times.size * cellHeight}px"
                                top = "0px"
                                left = "${dayIndex * cellWidth + timeWidth}px"
                                backgroundColor = cellColor
                            }
                            for ((index, current) in times.withIndex()) {
                                div {
                                    addClass("wc_cell")
                                    withStyle {
                                        border = "1px solid #000"
                                        position = "absolute"
                                        top = "${cellHeight * index}px"
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
                                        top = "${cellHeight * Random.nextInt(0, 30)}px"
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
       }

}





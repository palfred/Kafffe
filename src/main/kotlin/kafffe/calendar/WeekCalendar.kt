package kafffe.calendar

import kafffe.core.*
import org.w3c.dom.HTMLDivElement
import kotlin.browser.window
import kotlin.random.Random

class WeekCalendar(val eventsModel: Model<List<WeekEvent>>) : KafffeComponent() {

    var startTime: TimeOfDay = TimeOfDay(8, 0)
    var endTime: TimeOfDay = TimeOfDay(16, 0)
    var stepsPerHour: Int = 4 // must be divisor of 60
    private val minutesStep: Int get() = 60 / stepsPerHour

    var days = WeekDay.values()
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
    private val hourHeight = cellHeight * stepsPerHour
    private val minutesHeight = (cellHeight * stepsPerHour) / 60.0

    var headerColor: String by rerenderOnChange("#343")
    var cellColor: String by rerenderOnChange("#555")
    var textColor: String by rerenderOnChange("#fdf")
    var gridColor: String by rerenderOnChange("#000")

    /**
     * Holds the rendered events by event.id
     */
    private val eventElementMap: MutableMap<String, KafffeHtml<HTMLDivElement>> = mutableMapOf()

    /**
     * Holds the rendered events by event.id
     */
    private val dayContainerMap: MutableMap<WeekDay, KafffeHtml<HTMLDivElement>> = mutableMapOf()

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

            renderHeader()

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

                    renderTimeColumn()

                    for ((dayIndex, day) in days.withIndex()) {
                        renderDayColumn(day, dayIndex)
                    }
                }
            }
            renderEvents()
       }

    private fun KafffeHtml<HTMLDivElement>.renderHeader() {
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
                    text(day.daTitle)
                }
            }

        }
    }


    private fun KafffeHtml<HTMLDivElement>.renderTimeColumn() {
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
    }

    private fun KafffeHtml<HTMLDivElement>.renderDayColumn(day: WeekDay, dayIndex: Int) {
        div {
            dayContainerMap[day] = this
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
        }
    }

    private fun renderEvents() {
        removeEventNodesNotInModel()
        for ( event in eventsModel.data) {
            renderEvent(event)
        }
    }

    private fun removeEventNodesNotInModel() {
        val eventIds = eventsModel.data.map { it.id }
        eventElementMap.filterKeys { it !in eventIds }.forEach {
            it.value.element?.remove()
            eventElementMap.remove(it.key)
        }
    }

    private fun renderEvent(event : WeekEvent) {
        eventElementMap[event.id]?.element?.remove()

        val dayContainer: KafffeHtml<HTMLDivElement> = dayContainerMap[event.time.weekDay]!!
        val eventStart = event.time.startTime
        if (eventStart >= startTime && eventStart < endTime) {
            val minutesOffset = eventStart.minutesAfterDayStart - startTime.minutesAfterDayStart
            val topPx = minutesOffset * minutesHeight
            val durationPx = event.time.durationMinutes * minutesHeight
            dayContainer.div {
                eventElementMap[event.id] = this
                this.withStyle {
                    this.border = "1px solid black"
                    this.backgroundColor = "#060"
                    this.color = "#FFF"
                    this.opacity = "0.8"

                    // TODO handler overlaps
                    this.left = "5%"
                    this.width = "90%"

                    this.top = "${topPx}px"
                    this.height = "${durationPx}px"

                    this.position = "absolute"
                    this.padding = "0.1rem"
                    this.overflowX = "hidden"
                    this.overflowY = "hidden"
                }
                div {
                    element?.style?.fontWeight = "700"
                    this.text(event.title)
                }
                div {
                    element?.style?.whiteSpace = "pre-wrap"
                    this.text(event.description)
                }
            }
        }
    }

}





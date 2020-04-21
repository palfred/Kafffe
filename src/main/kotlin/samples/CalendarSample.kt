package samples

import kafffe.calendar.WeekCalendar
import kafffe.calendar.WeekEvent
import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import kafffe.core.Model
import org.w3c.dom.events.Event
import kotlin.browser.window

class CalendarSample : KafffeComponent() {

    private val calendar = addChild(WeekCalendar(Model.of(WeekEvent.demoEvents)))
    init {
        window.onresize = { event: Event ->
            calendar.noRerender {
                calendar.totalWidth = window.innerWidth - 30
                calendar.totalHeight = window.innerHeight - 100
            }
            calendar.rerender()
            event
        }
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                add(calendar.html)
            }

}
package samples

import kafffe.calendar.WeekCalendar
import kafffe.calendar.WeekCalendar2
import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import org.w3c.dom.events.Event
import kotlin.browser.window

class CalendarSample : KafffeComponent() {

    private val calendar = addChild(WeekCalendar())
    init {
        window.onresize = { event: Event ->
            calendar.noRerender {
                calendar.totalWidth = window.innerWidth - 30
                calendar.totalHeight = window.innerHeight - 80
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
package kafffe.core.modifiers

import kotlinx.browser.document
import org.w3c.dom.HTMLCollection
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

/**
 * MOdifies a HTMLELement to be moveable.
 * The handle element is selected as child with the give classname.
 * When the user click and drag the element is moed by a CSS translated.
 * Used on Modal content.
 */
class MoveableElementModifier(
   private val handleCssClass: String
) : HtmlElementModifier {

    private var isDragging: Boolean = false
    private var currentX = 0
    private var currentY = 0
    private var eventX = 0
    private var eventY = 0
    private lateinit var movingElement: HTMLElement
    private lateinit var handleElement: HTMLElement

    private val startDrag: (Event) -> Unit = { event ->
        if (event is MouseEvent) {
            isDragging = true
            eventX = event.clientX
            eventY= event.clientY
            document.addEventListener("mousemove", drag)
            document.addEventListener("mouseup", dragEnd)
        }
    }

    private val dragEnd: (Event) -> Unit = {
        if (isDragging) {
            isDragging = false
            // remove global event handlers
            document.removeEventListener("mousemove", drag)
            document.removeEventListener("mouseup", dragEnd)
        }
    }

    private val drag: (Event) -> Unit = { e ->
        if (e is MouseEvent) {
            e.preventDefault()
            currentX += (e.clientX - eventX)
            currentY += (e.clientY - eventY)
            eventX = e.clientX
            eventY = e.clientY
            movingElement.style.transform = "translate3d(${currentX}px, ${currentY}px, 0)"
        }
    }

    override fun modify(element: HTMLElement) {
        movingElement = element
        currentX = 0
        currentY = 0
        val byClassName: HTMLCollection = element.getElementsByClassName(handleCssClass)
        if (byClassName.length == 0) return
        handleElement = byClassName.item(0) as HTMLElement
        handleElement.style.cursor = "move"
        handleElement.addEventListener("mousedown", startDrag)
    }

}
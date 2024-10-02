package kafffe.utils

import externals.browser.IntersectionObserver
import externals.browser.IntersectionObserverEntry
import org.w3c.dom.Element


/**
 * Utilities for using browser observer. For now use of IntersectionObserver for visibilty of element.
 */
object DomObserverUtils {

    /**
     * Callback on first visibility on an HTMLElement, can be sued to dealy javascript to when the element in shown.
     * Some external javascript is only working if the element is in the DOM. Can also be used to make an efect when the element is shown.
     * The observer is disposed after the element is shown.
     */
    fun <T : Element> firstVisibleInViewport(htmlElement: T, whenVisible: (T) -> Unit) : IntersectionObserver {
        val observer =
            IntersectionObserver { entries: Array<IntersectionObserverEntry>, observer: IntersectionObserver ->
                entries.forEach { entry ->
                    if (entry.isIntersecting) {
                        whenVisible(htmlElement)
                        observer.disconnect();
                    }
                }
            }
        observer.observe(htmlElement)
        return observer
    }

    /**
     * Callback on whenever an HTMLElement is being made visible in the viewport.
     * Can also be used to make an effect whenever the element is shown, could probably also be used for infinite scrolling.
     * The caller should disposed the observer, when no longer used.
     */
    fun <T : Element> whenVisibleInViewport(htmlElement: T, whenVisible: (T) -> Unit) : IntersectionObserver {
        val observer =
            IntersectionObserver { entries: Array<IntersectionObserverEntry>, observer: IntersectionObserver ->
                entries.forEach { entry ->
                    if (entry.isIntersecting) {
                        whenVisible(htmlElement)
                    }
                }
            }
        observer.observe(htmlElement)
        return observer
    }
}
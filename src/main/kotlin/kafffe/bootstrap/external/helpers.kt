@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "DEPRECATION", "CAST_NEVER_SUCCEEDS")

package kafffe.bootstrap.external

import bootstrap.Popover
import bootstrap.Tooltip
import org.w3c.dom.HTMLElement

/**
 * Creates an empty java script obejct and gives access to it using interface (ie TooltipOption)
 */
fun <T> jsCreate() = js("({})") as T

enum class Placement {
    auto,
    top,
    bottom,
    left,
    right;

}

enum class Trigger {
    hover,
    click,
    focus,
    manual
}

inline fun <reified T : Enum<T>> fromDynamic(value: dynamic): T? {
    val strValue = value?.toString() ?: "NULL"
    for (eVal in enumValues<T>()) {
        if (strValue.equals(eVal.toString(), ignoreCase = true)) {
            return eVal
        }
    }
    return null
}

var Tooltip.Options.placementEnum: Placement?
    get() = fromDynamic<Placement>(placement)
    set(value) {
        placement = value.toString()
    }

var Tooltip.Options.triggerEnum: Trigger?
    get() = fromDynamic<Trigger>(trigger)
    set(value) {
        trigger = value.toString()
    }

var Tooltip.Options.triggersEnum: Set<Trigger>
    get() {
        val trigs = trigger?.toString()?.split(" ") ?: listOf()
        return trigs.map { fromDynamic<Trigger>(it.asDynamic()) }.filter { it != null }.map { it!! }.toSet()
    }
    set(value) {
        trigger = value.map { it.toString() }.joinToString(" ")
    }


enum class TooltipAction {
    toggle, hide, show, enable, disable, dispose, toggleEnabled, update;
}

fun createTooltip(element: HTMLElement) = Tooltip(element)
fun createTooltip(element: HTMLElement, options: Tooltip.Options) = Tooltip(element, options)

fun createPopover(element: HTMLElement) = Popover(element)
fun createPopover(element: HTMLElement, options: Popover.Options) = Popover(element, options)

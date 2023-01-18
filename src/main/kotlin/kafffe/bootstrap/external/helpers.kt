@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "DEPRECATION", "CAST_NEVER_SUCCEEDS")

package kafffe.bootstrap.external

import js.externals.jquery.JQuery
import js.externals.jquery.invoke
import js.externals.jquery.jQuery
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

/**
 * Creates an empty java script obejct and gives access to it using interface (ie TooltipOption)
 */
fun <T> jsCreate() = js("({})") as T

fun bsJquery(element: HTMLElement): BootstrapJQuery {
    val jq : JQuery = jQuery.invoke(element as Element)
    return jq as BootstrapJQuery
}
fun bsJquery(selector: String): BootstrapJQuery = jQuery.invoke(selector) as BootstrapJQuery

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

var TooltipOption.placementEnum: Placement?
    get() = fromDynamic<Placement>(placement)
    set(value) {
        placement = value.toString()
    }

var TooltipOption.triggerEnum: Trigger?
    get() = fromDynamic<Trigger>(trigger)
    set(value) {
        trigger = value.toString()
    }

var TooltipOption.triggersEnum: Set<Trigger>
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

fun BootstrapJQuery.tooltip(action: TooltipAction) = tooltip(action.toString())
fun BootstrapJQuery.popover(action: TooltipAction) = popover(action.toString())
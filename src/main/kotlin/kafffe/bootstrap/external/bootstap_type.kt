@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION", "NESTED_CLASS_IN_EXTERNAL_INTERFACE")

package kafffe.bootstrap.external

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

external interface Delay {
    var show: Number
    var hide: Number
}

external interface TooltipInstance<T : TooltipOption> {
    var config: T
    var element: Element
    var tip: HTMLElement
}

external interface OffsetsExtend {
    var popper: Any? get() = definedExternally; set(value) = definedExternally
    var reference: Any? get() = definedExternally; set(value) = definedExternally
}

external interface CarouselOption {
    var interval: dynamic /* Number | Boolean */ get() = definedExternally; set(value) = definedExternally
    var keyboard: Boolean? get() = definedExternally; set(value) = definedExternally
    var slide: dynamic /* Boolean | String /* "next" */ | String /* "prev" */ */ get() = definedExternally; set(value) = definedExternally
    var pause: dynamic /* Boolean | String /* "hover" */ */ get() = definedExternally; set(value) = definedExternally
    var wrap: Boolean? get() = definedExternally; set(value) = definedExternally
}

external interface CollapseOption {
    var parent: dynamic /* String | Element | global.JQuery<HTMLElement> */ get() = definedExternally; set(value) = definedExternally
    var toggle: Boolean? get() = definedExternally; set(value) = definedExternally
}

external interface DropdownOption {
    var offset: dynamic /* String | Number | (`this`: DropdownOption, offset: OffsetsExtend) -> OffsetsExtend */ get() = definedExternally; set(value) = definedExternally
    var flip: Boolean? get() = definedExternally; set(value) = definedExternally
    var boundary: dynamic /* Popper.Boundary | HTMLElement */ get() = definedExternally; set(value) = definedExternally
    var reference: dynamic /* HTMLElement | String /* "toggle" */ | String /* "parent" */ */ get() = definedExternally; set(value) = definedExternally
    var display: dynamic /* String /* "dynamic" */ | String /* "static" */ */ get() = definedExternally; set(value) = definedExternally
}

external interface ModalOption {
    var backdrop: dynamic /* Boolean | String /* "static" */ */ get() = definedExternally; set(value) = definedExternally
    var keyboard: Boolean? get() = definedExternally; set(value) = definedExternally
    var focus: Boolean? get() = definedExternally; set(value) = definedExternally
    var show: Boolean? get() = definedExternally; set(value) = definedExternally
}

external interface PopoverOption : TooltipOption {
    var content: dynamic /* String | Element | (`this`: Element) -> dynamic /* String | Element */ */ get() = definedExternally; set(value) = definedExternally
}

external interface ScrollspyOption {
    var method: dynamic /* String /* "auto" */ | String /* "offset" */ | String /* "position" */ */ get() = definedExternally; set(value) = definedExternally
    var offset: Number? get() = definedExternally; set(value) = definedExternally
    var target: dynamic /* String | Element */ get() = definedExternally; set(value) = definedExternally
}

external interface TooltipOption {
    var animation: Boolean? get() = definedExternally; set(value) = definedExternally
    var container: dynamic /* String | Boolean | Element */ get() = definedExternally; set(value) = definedExternally
    var delay: dynamic /* Number | Delay */ get() = definedExternally; set(value) = definedExternally
    var html: Boolean? get() = definedExternally; set(value) = definedExternally
    var placement: dynamic /* String /* "auto" */ | String /* "top" */ | String /* "bottom" */ | String /* "left" */ | String /* "right" */ | (`this`: TooltipInstance, node: HTMLElement, trigger: Element) -> dynamic /* String /* "auto" */ | String /* "top" */ | String /* "bottom" */ | String /* "left" */ | String /* "right" */ */ */ get() = definedExternally; set(value) = definedExternally
    var selector: dynamic /* String | Boolean */ get() = definedExternally; set(value) = definedExternally
    var template: String? get() = definedExternally; set(value) = definedExternally
    var title: dynamic /* String | Element | (`this`: Element) -> dynamic /* String | Element */ */ get() = definedExternally; set(value) = definedExternally
    var trigger: dynamic /* String /* "hover" */ | String /* "click" */ | String /* "focus" */ | String /* "manual" */ | String /* "click hover" */ | String /* "click focus" */ | String /* "hover focus" */ | String /* "click hover focus" */ */ get() = definedExternally; set(value) = definedExternally
    var offset: dynamic /* String | Number */ get() = definedExternally; set(value) = definedExternally
    var fallbackPlacement: dynamic /* Popper.Behavior | ReadonlyArray<Popper.Behavior> */ get() = definedExternally; set(value) = definedExternally
    var boundary: dynamic /* Popper.Boundary | HTMLElement */ get() = definedExternally; set(value) = definedExternally
}


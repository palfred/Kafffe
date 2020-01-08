@file:Suppress("DEPRECATION")
@file:JsQualifier("global")

package kafffe.bootstrap.external

external interface BootstrapJQuery {
    fun remove(): BootstrapJQuery
    fun alert(action: String? = definedExternally): BootstrapJQuery
    fun button(action: String): BootstrapJQuery
    fun carousel(action: Number): BootstrapJQuery
    fun carousel(action: String): BootstrapJQuery
    fun carousel(options: CarouselOption? = definedExternally): BootstrapJQuery
    fun collapse(action: String): BootstrapJQuery
    fun collapse(options: CollapseOption? = definedExternally): BootstrapJQuery
    fun dropdown(action: String): BootstrapJQuery
    fun dropdown(options: DropdownOption? = definedExternally): BootstrapJQuery
    fun modal(action: String): BootstrapJQuery
    fun modal(options: ModalOption? = definedExternally): BootstrapJQuery
    fun popover(action: String): BootstrapJQuery
    fun popover(options: PopoverOption? = definedExternally): BootstrapJQuery
    fun scrollspy(action: String): BootstrapJQuery
    fun scrollspy(options: ScrollspyOption? = definedExternally): BootstrapJQuery
    fun tab(action: String): BootstrapJQuery
    fun tooltip(action: String): BootstrapJQuery
    fun tooltip(options: TooltipOption? = definedExternally): BootstrapJQuery
    fun alert(): BootstrapJQuery
}


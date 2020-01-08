package kafffe.bootstrap

import kafffe.core.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * Bootstrap DivContainer intended for a cell content element
 */
class BootstrapRow() : DivContainer() {
    init {
        modifiers.add(CssClassModifier("row"))
    }

    fun addCell(child: KafffeComponent) {
        super.addChild(child)
    }

    fun cell(content: KafffeComponent, vararg colWidths: ColWidth) = BootstrapCell(content, colWidths.asIterable()).apply { addCell(this) }

}

fun bootstrapRowComponent(block: BootstrapRow.() -> Unit) = BootstrapRow().apply(block)

class BootstrapCell(content: KafffeComponent, colWidths: Iterable<ColWidth>) : DivContainer() {
    init {
        addChild(content)
        colWidths.forEach {
            modifiers.add(it.cssClassModifer)
        }
    }

}

/**
 * KafffeHtml helpers (when components are not Needed (probably most of the time where the cell content is not dynamically replaced
 */
/**
 * Creates HTML element with class "row", typically the cells inside taht wil be created by bootstrapCol
 */
fun <T : HTMLElement> KafffeHtml<T>.bootstrapRow(block: KafffeHtmlConsumer<HTMLDivElement> = {}) = createElement("div", block).also { it.addClass("row") }

/**
 * Creates HTML element with class "cells" inside ta bootstrapRow. The cell can have responsive col width(s).
 */
fun <T : HTMLElement> KafffeHtml<T>.bootstrapCol(vararg width: ColWidth, block: KafffeHtmlConsumer<HTMLDivElement> = {}) = createElement("div", block).also {
    for (w in width) {
        it.addClass(w.cssClass)
    }
}

/**
 * Creates HTML element with class "cells" inside ta bootstrapRow. The cell will have col-auto.
 */
fun <T : HTMLElement> KafffeHtml<T>.bootstrapColAuto(block: KafffeHtmlConsumer<HTMLDivElement> = {}) = createElement("div", block).also { it.addClass("col-auto") }

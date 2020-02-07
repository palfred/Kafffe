package kafffe.bootstrap

import kafffe.core.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTableCellElement
import org.w3c.dom.get

class BootstrapTableColumn<Data : Any>(val title: Model<String>) {
    var rowClick: Boolean = true
    var headerCell: (title: Model<String>) -> KafffeComponent = ::Label
    var contentCell: (row: Data, cell: HTMLTableCellElement) -> KafffeComponent = { _, _ -> Label("content goes here") }

    fun header() = headerCell(title)
    fun content(row: Data, cell: HTMLTableCellElement) = contentCell(row, cell)

}

enum class BootstrapTableStyles(val cssClass: String) {
    striped("table-striped"),
    bordered("table-bordered"),
    light("table-light"),
    dark("table-dark"),
    hover("table-hover");

    val cssClassModifer = CssClassModifier(cssClass)
}


class BootstrapTable<Data : Any>(data: Model<List<Data>>) : KafffeComponentWithModel<List<Data>>(data) {
    val columns = mutableListOf<BootstrapTableColumn<Data>>()
    val modifiersHeader = mutableListOf<HtmlElementModifier>()
    val modifiersBody = mutableListOf<HtmlElementModifier>()
    var rowClickHandler: (rowData: Data) -> Unit = {}

    companion object {
        fun <D : Any> create(data: List<D>, block: BootstrapTable<D>.() -> Any): BootstrapTable<D> = BootstrapTable<D>(Model.of(data)).apply { block() }
        fun <D : Any> create(data: Model<List<D>>, block: BootstrapTable<D>.() -> Any): BootstrapTable<D> = BootstrapTable<D>(data).apply { block() }
    }

    /**
     * Adds a column to this table, does not rerender.
     */
    fun colEx(title: Model<String>, content: (row: Data, cell: HTMLTableCellElement) -> KafffeComponent, header: (titel: Model<String>) -> KafffeComponent = ::Label): BootstrapTableColumn<Data> {
        val column = BootstrapTableColumn<Data>(title)
        with(column) {
            headerCell = header
            contentCell = content
        }
        columns.add(column)
        return column
    }

    fun col(title: Model<String>, content: (row: Data) -> KafffeComponent, header: (titel: Model<String>) -> KafffeComponent = ::Label): BootstrapTableColumn<Data> =
            colEx(title, { row, _ -> content(row) }, header)

    var data: List<Data> by delegateToModel()

    fun addStyle(style: BootstrapTableStyles) {
        modifiers.add(style.cssClassModifer)
    }

    fun applyDefaultStyle() {
        addStyle(BootstrapTableStyles.striped)
        addStyle(BootstrapTableStyles.bordered)
        addStyle(BootstrapTableStyles.hover)

    }

    // Helpers for establishing the KafffeCOmponent hierchy for the table cells
    private val cells = mutableListOf<KafffeComponent>()

    private fun addCell(cell: KafffeComponent) {
        cells.add(cell)
        addChild(cell)
    }

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        for (cell in cells) {
            removeChild(cell)
        }
        cells.clear()
        return table {
            with(element) {
                addClass("table")
                thead {
                    tr {
                        for (col in columns) {
                            th {
                                val headerCell = col.headerCell(col.title)
                                addCell(headerCell)
                                add(headerCell.html)
                            }
                        }
                    }
                }

                tbody {
                    for (row in data) {
                        tr {
                            for (col in columns) {
                                td {
                                    if (col.rowClick) {
                                        withElement {
                                            onclick = { rowClickHandler(row) }
                                        }
                                    }
                                    val contentCell = col.contentCell(row, this.element!!)
                                    addCell(contentCell)
                                    add(contentCell.html)
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    override fun modifyHtml(element: HTMLElement): HTMLElement {
        val elm = super.modifyHtml(element)
        val header = elm.getElementsByTagName("thead")
        header.get(0)?.let {
            for (mod in modifiersHeader) {
                if (it is HTMLElement) mod.modify(it)
            }
        }
        val body = elm.getElementsByTagName("tbody")
        body.get(0)?.let {
            for (mod in modifiersBody) {
                if (it is HTMLElement) mod.modify(it)
            }
        }

        return elm;
    }
}

package kafffe.bootstrap.form.listeditor

import kafffe.bootstrap.form.FormValueProvider
import kafffe.core.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import kotlinx.browser.window

abstract class ListEditor<T: Any>(model: Model<List<T>>) : KafffeComponentWithModel<List<T>>(model), FormValueProvider {
    protected var currentList: MutableList<T> = model.data.toMutableList()
    init {
        reloadListFromModel()
    }

    var haveButtons: Boolean = true
    var haveHeaders: Boolean = true
    var haveCreate: Boolean = true

    fun onlyEdit() {
        haveButtons = false
        haveHeaders = false
        haveCreate= false
    }

    fun reloadListFromModel() {
        currentList = model.data.toMutableList()
        rerender()
    }

    fun removeElement(element: T) {
        val ix = currentList.indexOf(element)
        removeElementAtIndex(ix)
    }

    fun removeElementAtIndex(index: Int) {
        if (index >= 0 && index < currentList.size) {
            currentList.removeAt(index)
        }
    }

    fun moveElement(element: T, offset: Int) {
        val ix = currentList.indexOf(element)
        val newIx = ix + offset
        focusAfterRerender = newIx
        if (ix != -1 && newIx in (0 until currentList.size)) {
            currentList.removeAt(ix)
            currentList.add(newIx, element)
        }
        rerender()
    }

    override fun updateValueModel() {
        // TODO remove empty elements?
        model.data = currentList.toList()
    }

    /**
     * Index to reclaim focus after rerender -1 = new row
     */
    var focusAfterRerender: Int = -2

   override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
       // make sure allways at least one element
       if (currentList.isEmpty()) {
           currentList.add(createNewElement())
       }

       return div {
            addClass("form-group")
            div {
                addClass("list-group")
                if (haveHeaders) {
                    columnHeaders()
                }
                currentList.forEachIndexed() { index, element ->
                    div {
                        addClass("form-inline")
                        elementEditor(element, index)
                        if (haveButtons) {
                            span {
                                addClass("btn-group")
                                button {
                                    addClass("btn btn-info")
                                    withElement {
                                        type = "button"
                                        onclick = {
                                            moveElement(element, -1)
                                        }
                                        disabled = (index == 0)
                                    }
                                    i {
                                        addClass("fas fa-arrow-up")
                                    }
                                }
                                button {
                                    addClass("btn btn-info")
                                    withElement {
                                        type = "button"
                                        onclick = {
                                            moveElement(element, +1)
                                        }
                                        disabled = (index >= currentList.size - 1)
                                    }
                                    i {
                                        addClass("fas fa-arrow-down")
                                    }
                                }
                                if (haveCreate) {
                                    button {
                                        addClass("btn btn-info")
                                        withElement {
                                            type = "button"
                                            onclick = { addElement(index + 1) }
                                            title = "Tilføj nedenfor"
                                        }
                                        i {
                                            addClass("fas fa-plus")
                                        }
                                    }
                                }
                                button {
                                    addClass("btn btn-warning")
                                    withElement {
                                        type = "button"
                                        onclick = {
                                            removeElement(element)
                                            rerender()
                                        }
                                    }
                                    i {
                                        addClass("fas fa-trash")
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
    }


    protected open fun elementKeyHandler(keyEvent: KeyboardEvent, element: T) {
        if (keyEvent.ctrlKey) {
            when (keyEvent.key) {
                "Enter" -> {
                    val ix = currentList.indexOf(element)
                    addElement(ix + 1)
                }
                "ArrowUp" -> {
                    keyEvent.preventDefault()
                    moveElement(element, -1)
                }
                "ArrowDown" -> {
                    keyEvent.preventDefault()
                    moveElement(element, 1)
                }
            }
        }
    }

    /**
     * Should create a new default element
     */
    protected abstract fun createNewElement() : T

    /**
     * Should create editor for new element together with createNewElement. Use elementKeyHandler to handle ie. ctrl-arrows for up and down movement.
     */
    protected abstract fun KafffeHtml<HTMLDivElement>.elementEditor(element: T, index: Int)

    protected fun HTMLElement.delayedFocus() {
        window.setTimeout({ this.focus() }, 300)
    }

    protected open fun KafffeHtml<HTMLDivElement>.columnHeaders() {
    }

    protected open fun addElement(ix: Int) {
        focusAfterRerender = ix
        val element = createNewElement()
        currentList.add(ix, element)
        rerender()
    }
    protected open fun addElement() {
        focusAfterRerender = -1
        val element = createNewElement()
        currentList.add(element)
        rerender()
    }

    protected open fun addElement(element: T) {
        focusAfterRerender = -1
        currentList.add(element)
        rerender()
    }

    /**
     * To be called when the add comes from a key event, to prevent default and do a rerender
     */
    protected fun onNewKey(keyEvent: KeyboardEvent, ix: Int) {
        addElement(ix)
        keyEvent.preventDefault()
    }

    /**
     * Tobe called when the move comes from a key event, to prevent default and do a rerender
     */
    protected fun onMoveKey(keyEvent: KeyboardEvent, element: T, offset: Int ) {
        moveElement(element, offset)
        keyEvent.preventDefault()
    }


}

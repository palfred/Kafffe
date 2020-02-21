package kafffe.bootstrap.form.listeditor

import kafffe.bootstrap.form.FormValueProvider
import kafffe.core.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window

abstract class ListEditor<T: Any>(model: Model<List<T>>) : KafffeComponentWithModel<List<T>>(model), FormValueProvider {
    protected var currentList: MutableList<T> = model.data.toMutableList()

    fun reloadListFromModel() {
        currentList = model.data.toMutableList()
        rerender()
    }

    private fun removeField(t: T) {
        val ix = currentList.indexOf(t)
        if (ix != -1) {
            currentList.removeAt(ix)
        }
    }

    private fun moveField(t: T, offset: Int) {
        val ix = currentList.indexOf(t)
        val newIx = ix + offset
        focusAfterRerender = newIx
        if (ix != -1 && newIx in (0 until currentList.size)) {
            currentList.removeAt(ix)
            currentList.add(newIx, t)
        }
    }

    override fun updateValueModel() {
        model.data = currentList.toList()
    }

    /**
     * Index to reclaim focus after rerender -1 = new row
     */
    protected var focusAfterRerender: Int = -2

   override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        return div {
            addClass("form-group")
            div {
                addClass("list-group")
                columnHeaders()
                currentList.forEachIndexed() { index, element ->
                    div {
                        addClass("form-inline")
                        elementEditor(element, index)

                        span {
                            addClass("btn-group")
                            button {
                                addClass("btn btn-info")
                                withElement {
                                    type = "button"
                                    onclick = {
                                        moveField(element, -1)
                                        rerender()
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
                                        moveField(element, +1)
                                        rerender()
                                    }
                                    disabled = (index >= currentList.size - 1)
                                }
                                i {
                                    addClass("fas fa-arrow-down")
                                }
                            }
                            button {
                                addClass("btn btn-warning")
                                withElement {
                                    type = "button"
                                    onclick = {
                                        removeField(element)
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
                div {
                    addClass("form-inline")
                    newElementEditor()
                    button {
                        addClass("btn btn-info")
                        withElement {
                            type = "button"
                            onclick = {addElement()}
                            }
                        i {
                            addClass("fas fa-plus")
                        }
                    }

                }

            }
        }
    }

    protected open fun newElementKeyHandler(keyEvent: KeyboardEvent) {
        if (keyEvent.key == "Enter") {
            addElement()
        }
    }

    protected open fun elementKeyHandler(keyEvent: KeyboardEvent, t: T) {
        if (keyEvent.ctrlKey) {
            when (keyEvent.key) {
                "ArrowUp" -> {
                    keyEvent.preventDefault()
                    moveField(t, -1)
                    rerender()
                }
                "ArrowDown" -> {
                    keyEvent.preventDefault()
                    moveField(t, 1)
                    rerender()
                }
            }
        }
    }

    /**
     * Should create a new element based on the input on the newElementEditor.
     */
    protected abstract fun createNewElement() : T

    /**
     * Should create editor for new element together with createNewElement. Use newElementKeyHandler to handle ie. enter for add.
     */
    protected abstract fun KafffeHtml<HTMLDivElement>.newElementEditor()

    /**
     * Should create editor for new element together with createNewElement. Use elementKeyHandler to handle ie. ctrl-arrows for up and down movement.
     */
    protected abstract fun KafffeHtml<HTMLDivElement>.elementEditor(element: T, index: Int)

    protected fun HTMLElement.delayedFocus() {
        window.setTimeout({ this.focus() }, 300)
    }

    protected open fun KafffeHtml<HTMLDivElement>.columnHeaders() {
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

}

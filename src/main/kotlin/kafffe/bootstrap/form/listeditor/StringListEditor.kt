package kafffe.bootstrap.form.listeditor

import kafffe.core.KafffeHtml
import kafffe.core.Model
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement

class StringListEditor(model: Model<List<String>>) : ListEditor<String>(model) {
    private lateinit var newInput: HTMLInputElement

    override fun createNewElement(): String = newInput.value

    override fun KafffeHtml<HTMLDivElement>.elementEditor(listElement: String, index: Int) {
        input {
            addClass("form-control")
            withElement {
                value = listElement
                type = "text"
                onchange = { if (value.isNotBlank()) {currentList[index] = value}}
                onkeydown = { keyEvent -> elementKeyHandler(keyEvent, listElement)}
                if (index == focusAfterRerender) {delayedFocus()}
            }
        }
    }
}
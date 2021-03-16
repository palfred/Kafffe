package kafffe.bootstrap.form

import kafffe.bootstrap.BootstrapButton
import kafffe.bootstrap.ColWidth
import kafffe.core.*
import kotlin.reflect.KProperty1

/**
 * Interface for form and form components containers like form layout and fieldsets- To be used to build child components.
 *
 */
interface FormComponentConsumer<T : Any> {
    /**
     * Reference to the form this is part of.
     */
    val form: BootstrapForm<*>

    /**
     * Adds a child component to the hierarchy
     */
    fun <Child : KafffeComponent> addChild(child: Child): Child

    /**
     * The current label strategy used to create labels fro components base on a name key.
     */
    var labelStrategy: LabelStrategy

    /**
     * Decoration of inputs. Default wraps with formgroup with label and validation feedback.
     * NOT INTENDED to be called from outside use #decoratorAndAdd or #decorateAndAddComponent to call this and also *add* to the this consumer
     */
    var inputDecorator: (labelModel: Model<String>, inputComp: FormInput) -> KafffeComponent

    /**
     * The model of the form or sub form that is used to construct submodels for input compoennts
     */
    abstract var model: Model<T>

    /**
     * Applies inputDecorator to input component and adds.
     */
    fun decorateAndAdd(labelModel: Model<String>, component: FormInput)  {
        val wrapper = inputDecorator(labelModel, component)
        addChild(wrapper)
    }

    /**
     * Applies inputDecorator to input component and adds.
     */
    fun decorateAndAddComponent(labelModel: Model<String>, component: KafffeComponent) {
        decorateAndAdd(labelModel, KaffeeComponentAsInputAdapter(component))
    }

    fun input(idInput: String, labelModel: Model<String>, valueModel: Model<String>): InputString {
        val input = InputString(idInput, valueModel)
        decorateAndAdd(labelModel, input)
        return input
    }

    /**
     * Property based
     */
    fun input(property: KProperty1<T, String>): InputString =
        input(property.name, labelStrategy.label(property.name), model.property(property))


    fun inputNum(idInput: String, labelModel: Model<String>, valueModel: Model<Int>): InputInt {
        val input = InputInt(idInput, valueModel)
        decorateAndAdd(labelModel, input)
        return input
    }

    fun inputNum(property: KProperty1<T, Int>): InputInt =
        inputNum(property.name, labelStrategy.label(property.name), model.property(property))


    fun inputLong(idInput: String, labelModel: Model<String>, valueModel: Model<Long>): InputLong {
        val input = InputLong(idInput, valueModel)
        decorateAndAdd(labelModel, input)
        return input
    }

    fun inputLong(property: KProperty1<T, Long>): InputLong =
        inputLong(property.name, labelStrategy.label(property.name), model.property(property))


    fun readonly(idInput: String, labelModel: Model<String>, valueModel: Model<String>): InputString =
        input(idInput, labelModel, valueModel).apply { readOnly = true }

    fun readonly(property: KProperty1<T, String>): InputString =
        readonly(property.name, labelStrategy.label(property.name), model.property(property))

    fun readonly(labelModel: Model<String>, valueModel: Model<String>): InputString =
        readonly(labelModel.data, labelModel, valueModel)

    fun submit(label: Model<String>): BootstrapButton {
        return BootstrapButton(label, {}).also {
            it.btnType = ButtonType.submit
            addChild(it)
        }
    }

    fun submit(labelKey: String = "ok"): BootstrapButton = submit(labelStrategy.label(labelKey))

    fun submit(label: Model<String>, onOk: () -> Unit, onError: () -> Unit = {}): BootstrapButton {
        return BootstrapButton(label, { form.processForm(onOk, onError) }).also {
            addChild(it)
        }
    }

    fun submit(labelKey: String = "ok", onOk: () -> Unit, onError: () -> Unit = {}): BootstrapButton {
        return submit(labelStrategy.label(labelKey), onOk, onError)
    }

    fun cancel(label: Model<String>): BootstrapButton {
        return BootstrapButton(label, { form.onCancel() }).also {
            it.btnType = ButtonType.button
            addChild(it)
        }
    }

    fun cancel(labelKey: String = "cancel"): BootstrapButton = cancel(labelStrategy.label(labelKey))

    fun button(label: Model<String>, onClick: (source: BootstrapButton) -> Unit): BootstrapButton {
        return BootstrapButton(label, onClick).also {
            it.btnType = ButtonType.button
            addChild(it)
        }
    }

    fun button(labelKey: String, onClick: (source: BootstrapButton) -> Unit): BootstrapButton =
        button(labelStrategy.label(labelKey), onClick)

    // Layout
    fun row(block: FormLayout<T>.() -> Unit): FormLayout<T> = row(model, block)

    fun <S : Any> row(subModel: Model<S>, block: FormLayout<S>.() -> Unit): FormLayout<S> =
        FormLayout<S>(this, subModel).also {
            it.modifiers.add(CssClassModifier("form-row"))
            it.block()
            addChild(it)
        }

    fun group(block: FormLayout<T>.() -> Unit): FormLayout<T> = group(model, block)

    fun <S : Any> group(subModel: Model<S>, block: FormLayout<S>.() -> Unit): FormLayout<S> =
        FormLayout<S>(this, subModel).also {
            it.modifiers.add(CssClassModifier("form-group"))
            it.block()
            addChild(it)
        }

    fun <S : Any> col1(
        width: Array<out ColWidth>,
        subModel: Model<S>,
        block: FormLayout<S>.() -> Unit
    ): FormLayout<S> = FormLayout<S>(this, subModel).also {
        for (w in width) {
            it.modifiers.add(w.cssClassModifer)
        }
        it.block()
        addChild(it)
    }


    fun <S : Any> col(
        vararg width: ColWidth,
        subModel: Model<S>,
        block: FormLayout<S>.() -> Unit
    ): FormLayout<S> = col1(width, subModel, block)

    fun col(vararg width: ColWidth, block: FormLayout<T>.() -> Unit): FormLayout<T> =
        col1(width, model, block)

    fun legend(textModel: Model<String>) = LegendComponent(textModel).also { addChild(it) }
    fun legend(textKey: String) = legend(labelStrategy.label(textKey))

    fun label(textModel: Model<String>) = Label(textModel).also { addChild(it) }
    fun label(textKey: String) = label(labelStrategy.label(textKey))

}
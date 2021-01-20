package samples

import kafffe.bootstrap.*
import kafffe.bootstrap.form.*
import kafffe.core.*
import kotlinx.browser.window

class FormSample : KafffeComponent() {

    data class Person(var firstName: String, var lastName: String, var age: Int, var choice: String, var tags: List<String>)

    val choices = Model.of(listOf("Zero", "First", "Second", "Third", "Fourth", "Fifth", "Other"))

    val personModel = Model.of<Person>(Person("Jens", "Hansen", 45, "Second", listOf("Fourth", "Other")))
    val form = BootstrapForm<Person>(personModel).apply {
        inputDecorator = { labelModel, inputComp ->
            FormInputGroupDecorator(labelModel, inputComp).apply { useTooltipValidationMessages = true }
        }
        // We need some hgap because we do not apply whitespace "\n" between buttons.
        modifiers.add(CssClassModifier("hgap-2"))
        input("fn", Model.of("First Name"), personModel.property(Person::firstName)).apply {
            validationMessageModel = Model.of("First Name is Required")
            required = true
        }
        input(Person::lastName).apply {
            validationMessageModel = Model.of("Last Name is Required and must be Petersen or Jensen")
            required = true
            validationPattern = "Petersen|Jensen"
        }
        inputNum(Person::age).apply {
            validationMessageModel = Model.of("Age must be between 0 and 200")
            required = true
            minimum = 0
            maximum = 200
        }

        row {
            col(ResponsiveSize.sm.col(6)) {
                useBorderedLegend(Model.of("Multiple"))
                dropdownMultiple(Person::tags, choices).apply {
                    // Example of customised look
                    modifiersValue.add { choice ->
                        when (choice) {
                            "Other" -> StyleModifier { fontStyle = "italic"; fontSize = "1.5em" }
                            else -> StyleModifier {}
                        }

                    }
                }
                editSelectMultiple(Person::tags, choices).apply {
                    // Example of customised look classes and others
                    valueCssClasses = { choice ->
                        when (choice) {
                            "First" -> "badge badge-secondary text-white ml-1"
                            "Second" -> "badge badge-success text-white ml-1"
                            else -> valueCssClassDefault
                        }
                    }
                    modifiersValue.add { choice ->
                        when (choice) {
                            "Other" -> StyleModifier { fontStyle = "italic"; fontSize = "1.5em" }
                            else -> StyleModifier {}
                        }

                    }
                }

            }

            col(ResponsiveSize.sm.col(6)) {
                useBorderedLegend(Model.of("Single"))

                dropdown(Person::choice, choices)
                editSelectSingle(Person::choice, choices).apply {
                    // Example of customised look classes and others
                    valueCssClasses = { choice ->
                        when (choice) {
                            "First" -> "badge badge-secondary text-white ml-1"
                            "Second" -> "badge badge-success text-white ml-1"
                            else -> valueCssClassDefault
                        }
                    }
                    modifiersValue.add { choice ->
                        when (choice) {
                            "Other" -> StyleModifier { fontStyle = "italic"; fontSize = "1.5em" }
                            else -> StyleModifier {}
                        }

                    }
                }

                val drop2 = DropdownString("selId2", Model.of("Second"), choices)
                drop2.useCustom = false
                inputDecorator(Model.of("Custom"), drop2)
            }
        }

        inputDecorator(Model.of("Custom validation"), CustomInput("custom", Model.of("")))

        submit("testIt").color = BasicColor.primary
        submit("showIt", ::alertSubmit).color = BasicColor.secondary
        submit("confirm It", ::confirmSubmit).color = BasicColor.info

        onSubmitOk = {
            //Default method to be called if not specified on the submit button.
            println("Form Model data is now: ${personModel.data} ")
            personModel.data = Person("Test", "Successful", 42, "Second", listOf())
        }
    }.also { addChild(it) }

    private fun alertSubmit() {
        window.alert("Form Model data is now: ${personModel.data} ")
    }

    private fun confirmSubmit() {
        Modal.confirm(Model.of("Form Model"), Model.of("Is now: ${personModel.data} "), yesHandler = { println("Yes") })
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                bootstrapRow {
                    bootstrapCol(ColWidth(ResponsiveSize.sm, 6)) {
                        add(form.html)
                    }
                }
            }
}

class CustomInput(idInput: String, model: Model<String>) : InputString (idInput, model) {
    init {
        modifiers.add(HtmlElementModifier.create {
            htmlInput.formNoValidate = true
        })
    }
    override fun validate(): Boolean {
        val valid = htmlInput.value.equals("Peter", ignoreCase = true)
        htmlInput.applyInputValidCssClasses(valid)
        return valid
    }
    override var validationMessageModel: Model<String> = Model.of("Custom message for field that needs to Peter")
}
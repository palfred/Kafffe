package kafffe.messages

import kafffe.core.FunctionalModel
import kafffe.core.FunctionalSubModel
import kafffe.core.Model
import kafffe.core.ServiceRegistry
import kotlin.browser.window
import kotlin.js.Date
import kotlin.reflect.KProperty1

/**
 * I18n language support for Kafffe.
 * Add Messages derivation and overide properties (and message functions). The new language also need to be included in allLanguages.
 */
interface Messages {
    companion object {
        var currentLanguage: String = window.navigator.language
            set(value) {
                field = value
            }

        fun registerMessages() {
            ServiceRegistry.register("Messages_en", Messages_en())
            ServiceRegistry.register("Messages_da", Messages_da())
        }

        val allLanguages: List<Messages>
            get() = ServiceRegistry.findAll<Messages>()

        private var cache: Messages? = null
        private var cachedLanguage: String? = null

        fun get(): Messages {
            if (cachedLanguage != currentLanguage) {
                cache = get(currentLanguage)
                cachedLanguage = currentLanguage
            }
            return cache!!
        }

        fun get(language: CharSequence): Messages {
            return allLanguages.find { language.startsWith(it.language) } ?: Messages_en()
        }

        val dateOptions = dateLocaleOptions {
            month = "2-digit"
            day = "2-digit"
            year = "numeric"
        }
        val dateTimeOptions = dateLocaleOptions {
            month = "2-digit"
            day = "2-digit"
            year = "numeric"
            hour = "2-digit"
            minute = "2-digit"
        }
        val timeOptions = dateLocaleOptions {
            hour = "2-digit"
            minute = "2-digit"
        }

        fun Date.formatDateTime() = toLocaleString(get().locale, dateTimeOptions)
        fun Date.formatDate() = toLocaleDateString(get().locale, dateOptions)
        fun Date.formatTime() = toLocaleTimeString(get().locale, timeOptions)
    }

    val language: String
    val locale: String
    val languageName: String
    val smallFlag: String
    val languageSelect: String
    val yes: String
    val no: String
    val ok: String
    val cancel: String
    val accept: String
    val decline: String
    val load: String
    val action: String
    val details: String
    val save: String
    val delete: String

    val nav_about: String

    val id: String
    val name: String
    val description: String
    val timestamp: String

    val validation_required: String

}

val messagesModel: Model<Messages> = FunctionalModel<Messages>(getData = Messages.Companion::get)

fun i18nText(prop: KProperty1<Messages, String>): Model<String> {
    val getMessage: (Model<Messages>) -> String = {
        val messages = messagesModel.data
        prop.get(messages)
    }
    return FunctionalSubModel<String, Messages>(messagesModel, getData = getMessage)
}




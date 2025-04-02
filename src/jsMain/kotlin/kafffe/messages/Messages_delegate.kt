package kafffe.messages

import kotlin.js.Json
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class Messages_delegate(val json: Json) : Messages_en() {
    class MapDelegate : ReadOnlyProperty<Messages_delegate, String> {
        override fun getValue(thisRef: Messages_delegate, property: KProperty<*>): String =
                (thisRef.json.get(property.name) ?: "Missing: ${property.name}") as String
    }

    override val locale by MapDelegate()
    override val language by MapDelegate()
    override val languageName by MapDelegate()
    override val smallFlag by MapDelegate()
    override val languageSelect by MapDelegate()

    override val yes by MapDelegate()
    override val no by MapDelegate()
    override val ok by MapDelegate()
    override val cancel by MapDelegate()
    override val accept by MapDelegate()
    override val decline by MapDelegate()
    override val save by MapDelegate()
    override val delete by MapDelegate()

    override val load by MapDelegate()
    override val action by MapDelegate()
    override val details by MapDelegate()
    override val nav_about by MapDelegate()

    override val id by MapDelegate()
    override val name by MapDelegate()
    override val description by MapDelegate()
    override val timestamp by MapDelegate()

    override val validation_required by MapDelegate()

    override val countOutOf  by MapDelegate()
    override val pageSize by MapDelegate()

}
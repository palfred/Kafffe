package kafffe.core

/**
 * General interface and classes for Navigation support in Kafffe
 * The basic idea is to have navgation target that support nvagate to a path that could like a rest like path ie "/company/<cloud partners id>/employee/list"
 * This could be handled by a navigation target that understands company + company id and that delegate to sub navigation target taht understands the ramining part of the path.
 **/
data class NavigationPath(val elements: List<String>, val absolute: Boolean = false) {

    companion object {
        const val delimiter: Char = '/'

        fun fromString(pathString: String, delimiter: Char = '/'): NavigationPath {
            val elms = pathString.split(delimiter)
            if (elms.first().isEmpty()) {
                return NavigationPath(elms.subList(1, elms.size), true)
            } else {
                return NavigationPath(elms, false)
            }
        }
    }

    val empty = elements.isEmpty()
    val head get() = elements.first()
    val rest get() = NavigationPath(elements.subList(1, elements.size), false)

    fun sub(pathElement: String) = NavigationPath(elements + pathElement, absolute)

    override fun toString(): String {
        val pathStr = elements.joinToString(separator = delimiter.toString()) { it }
        if (absolute) {
            return delimiter + pathStr
        } else {
            return pathStr
        }
    }

    fun startsWith(path: NavigationPath): Boolean {
        if (this.elements.size >= path.elements.size) {
            return this.elements.subList(0, path.elements.size) == path.elements
        }
        return false
    }
}

interface NavigationTarget {
    fun navigateTo(path: NavigationPath)
}


/**
 * May construct component, create sub navigation and/or set new ComponentConsumer ( = container)
 */
typealias NavigationSetupOnNavigateTo = NavigationElement.(path: NavigationPath) -> KafffeComponent?

typealias ComponentConsumer = (KafffeComponent) -> Unit

open class NavigationElement(val name: String) {
    companion object {
        fun create(name: String, block: NavigationElement.() -> Unit = {}) = NavigationElement(name).apply { block() }
    }

    var parent: NavigationElement? = null
    var componentConsumer: ComponentConsumer? = null
    /**
     * May construct compoonet, create sub navigation and/or set new ComponentConsumer ( = container)
     */
    var setupOnNavigateTo: NavigationSetupOnNavigateTo? = null

    private val childMap = mutableMapOf<String, NavigationElement>()
    val children get() = childMap.values

    fun add(element: NavigationElement, block: NavigationElement.() -> Unit = {}) {
        childMap.put(element.name, element)
        element.parent = this
        element.block()
    }

    fun sub(name: String, block: NavigationElement.() -> Unit = {}) = add(NavigationElement(name), block)

    fun component(name: String, setup: NavigationSetupOnNavigateTo) {
        add(NavigationElement(name).apply {
            setupOnNavigateTo = setup
        })
    }

    fun componentDynamicAll(name: String, setup: NavigationSetupOnNavigateTo) {
        val dynamicElement = object : NavigationElement(name) {
            override fun dynamicMatch(path: String) = true
        }
        dynamicElement.setupOnNavigateTo = setup
        add(dynamicElement)
    }

    fun remove(elementName: String) = childMap.remove(elementName)
    fun remove(element: NavigationElement) = remove(element.name)

    /**
     * Dynamic matches is intended for path elements that represent data like an id.
     */
    open fun dynamicMatch(path: String): Boolean = false

    open fun navigateTo(path: NavigationPath) {
        beforeNavigateToChild(path)
        navigateToChild(path.rest)
    }

    open protected fun navigateToChild(path: NavigationPath) {
        if (!path.empty) {
            if (childMap.containsKey(path.head)) {
                childMap[path.head]?.navigateTo(path)
            } else {
                // Look for dynamic match
                children.forEach {
                    if (it.dynamicMatch(path.head)) {
                        it.navigateTo(path)
                    }
                }
            }
        }
    }

    open fun beforeNavigateToChild(path: NavigationPath): KafffeComponent? {
        val setup = setupOnNavigateTo
        if (setup != null) {
            @Suppress("UNUSED_EXPRESSION")
            val comp = setup(path)
            componentReceiver()?.let {
                if (comp != null) {
                    it(comp)
                }
            }
            return comp;
        }
        return null;
    }

    val root: NavigationElement
        get() = if (parent == null) this else parent!!.root

    private fun componentReceiver(): ComponentConsumer? = if (componentConsumer != null) componentConsumer else parent?.componentReceiver()
}

fun NavigationExampleSetup() {
    val container = KafffeComponent()
    NavigationElement.create("root") {
        componentConsumer = { container.addChild(it) }
        component("about") { Label("About") }
        sub("customer") {
            setupOnNavigateTo = {
                Label("Customer DivContainer and Navigation")
                // SETUP componentReceiver and sub Navigation in here in place of outside
            }
            component("list", { Label("List Customer") })
            add(element = object : NavigationElement("customerid") {
                override fun dynamicMatch(path: String) = true;
            })
            component("edit") { Label("Edit Customer") }
        }
    }
}
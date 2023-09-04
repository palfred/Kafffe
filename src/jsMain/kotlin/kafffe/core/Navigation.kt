package kafffe.core

/**
 * General interface and classes for Navigation support in Kafffe
 * The basic idea is to have navgation target that support navigate to a path that could like a rest like path ie "/company/<cloud partners id>/employee/list"
 * This could be handled by a navigation target that understands company + company id and that delegate to sub navigation target that understands the ramining part of the path.
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


typealias ComponentConsumer = (KafffeComponent) -> Unit
typealias NavigateTo = NavigationElement.(path: NavigationPath) -> Unit
typealias NavigateToComponent = NavigationElement.(path: NavigationPath) -> KafffeComponent

open class NavigationElement(val name: String) {
    companion object {
        fun create(name: String, block: NavigationElement.() -> Unit = {}) = NavigationElement(name).apply { block() }
    }

    var parent: NavigationElement? = null
    var componentNavigator: ComponentConsumer? = null

    /**
     * Called on navigation to. May construct sub navigation or navigation to component.
     * @see #consumeComponent
     */
    var onNavigateTo: NavigateTo = {}

    private val childMap = mutableMapOf<String, NavigationElement>()
    val children get() = childMap.values

    fun add(element: NavigationElement, block: NavigationElement.() -> Unit = {}) {
        childMap.put(element.name, element)
        element.parent = this
        element.block()
    }

    fun sub(name: String, block: NavigationElement.() -> Unit = {}) = add(NavigationElement(name), block)

    fun dynamicAll(name: String, navigation: NavigateTo) {
        val dynamicElement = object : NavigationElement(name) {
            override fun dynamicMatch(path: String) = true
        }
        dynamicElement.onNavigateTo = navigation
        add(dynamicElement)
    }

    fun component(name: String, navigation: NavigateToComponent) {
        add(NavigationElement(name).apply {
            onNavigateTo = componentNavigation(navigation)
        })
    }

    // convert component navigation to simple navigation
    private fun componentNavigation(navigation: NavigateToComponent): NavigationElement.(NavigationPath) -> Unit {
        return { path: NavigationPath ->
            val comp = navigation(path)
            navigateToComponent(comp)
        }
    }

    fun componentDynamicAll(name: String, navigation: NavigateToComponent) {
        val dynamicElement = object : NavigationElement(name) {
            override fun dynamicMatch(path: String) = true
        }
        dynamicElement.onNavigateTo = componentNavigation(navigation)
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

    open fun beforeNavigateToChild(path: NavigationPath) {
        onNavigateTo(path)
    }

    val root: NavigationElement
        get() = if (parent == null) this else parent!!.root

    private fun componentConsumer(): ComponentConsumer? = if (componentNavigator != null) componentNavigator else parent?.componentConsumer()

    /**
     * "Navigation" to component
     */
    fun navigateToComponent(comp : KafffeComponent) = componentConsumer()?.let{it(comp)}
}

fun NavigationExampleSetup() {
    val container = KafffeComponent()
    NavigationElement.create("root") {
        componentNavigator = { container.addChild(it) }
        component("about") { Label("About") }
        sub("customer") {
            onNavigateTo = {
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
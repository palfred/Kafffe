package kafffe.core

import org.w3c.dom.HTMLElement
import kotlin.reflect.KClass

/**
 * Base Component wrapping HTML rendering and utilities for this
 */
open class KafffeComponent {
    /* hierarchy */
    var parent: KafffeComponent? = null
        set(value) {
            if (root() != null && value == null) {
                // we only do detach if we are part of hierarchy with root
                detachHierarchy()
            }
            field = value
            if (root() != null && value != null) {
                // we only do attach if we are part of hierarchy with root
                attachHierarchy()
            }
        }

    private val _children = mutableListOf<KafffeComponent>()
    val children: List<KafffeComponent> = _children

    open fun <Child : KafffeComponent> addChild(child: Child): Child {
        _children.add(child)
        child.parent = this;
        return child
    }

    open fun removeChild(child: KafffeComponent) {
        _children.remove(child)
        child.parent = null;
    }

    fun root(): RootComponent? {
        val p = parent
        return when (p) {
            null -> null
            is RootComponent -> p
            else -> p.root()
        }
    }

    fun <T : KafffeComponent> ancestorOfType(aType: KClass<T>): T? {
        val p = parent
        return if (p == null) null
        else if (aType.isInstance(p)) p.unsafeCast<T>()
        else p.ancestorOfType(aType)
    }

    val modifiers = mutableListOf<HtmlElementModifier>()

    private var _html: HTMLElement? = null

    var html: HTMLElement
        set(newValue) {
            val oldValue = _html;
            if (oldValue == null) {
            } else {
                val parentNode = oldValue.parentNode
                if (parentNode != null) {
                    parentNode.replaceChild(newValue, oldValue)
                }
            }
            _html = newValue
        }
        get() {
            _html = _html ?: render()
            return _html!!
        }

    val isRendered: Boolean get() = _html != null

    /**
     * Use this to control whether rendering/rerendering is enable for this component hiearchy
     */
    var renderingEnabled = true
    val isRenderingEnabled: Boolean
        get() = renderingEnabled && (parent?.isRenderingEnabled ?: true)

    /**
     * Marks this as not rendered (forget of previous rendered HTML
     */
    fun setNotRendered() {
        if (isRendered) {
            _html = null
        }
    }

    /**
     * Marks this and all children as not rendered (forget of previous rendered HTML)
     */
    fun setNotRenderedRecursive() {
        if (isRendered) {
            _html = null
            for (child in children) {
                child.setNotRenderedRecursive()
            }
        }
    }

    protected fun render(): HTMLElement = modifyHtml(createHtml())

    protected open fun modifyHtml(element: HTMLElement): HTMLElement {
        modifiers.forEach { it.modify(element) }
        return element
    }

    /**
     * Creates the basic HTMLELements (and children) needed for this component. Override this for concrete components
     *  or override kafffeHtml instead to to use the KafffeHtml DSL
     */
    protected open fun createHtml(): HTMLElement {
        return htmlStart.kafffeHtml().element!!
    }

    /**
     * Creates the basic HTMLELements (and children) needed for this component using the KafffeHtml DSL
     */
    protected open fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
      return div { children.forEach { add(it.html) } }
    }
    
    


    fun rerender() {
        if (isRendered && isRenderingEnabled) {
            html = render()
        }
    }

    fun rerenderRecursive() {
        if (isRendered) {
            for (child in children) {
                child.setNotRenderedRecursive()
            }
            html = render()
        }
    }


    /**
     * Visit all children recursive and do block on child before grand children (depth first).
     */
    fun visitChildrenRecusive(block: KafffeComponent.() -> Unit) {
        for (child in children) {
            child.block()
            child.visitChildrenRecusive(block)
        }
    }

    /**
     * This components is being attached to a component hierarchy.
     * Override this to add listeners to models and other components needed.
     */
    open fun attach() {}

    /**
     * This components is being detached from a component hierarchy.
     * Override this to remove listeners to models and other components setup in attach.
     */
    open fun detach() {}

    fun attachChildrenRecursive() = visitChildrenRecusive(KafffeComponent::attach)

    fun detachChildrenRecursive() = visitChildrenRecusive(KafffeComponent::detach)

    fun attachHierarchy() {
        attachChildrenRecursive()
        attach()
    }

    fun detachHierarchy() {
        detach()
        detachChildrenRecursive()
    }

    /**
     * Do some operation with this component hierachy detached
     */
    fun doDetached(block: KafffeComponent.() -> Unit) {
        try {
            detachHierarchy()
            @Suppress("UNUSED_EXPRESSION")
            block()
        } finally {
            attachHierarchy()
        }
    }

    /**
     * Do some operations without rerendering (ie because of model changes)
     */
    fun noRerender(block: KafffeComponent.() -> Unit) {
        try {
            renderingEnabled = false
            @Suppress("UNUSED_EXPRESSION")
            block()
        } finally {
            renderingEnabled = true
        }
    }

    /**
     * Reapply modifiers.
     */
    fun remodify() {
        modifyHtml(html);
    }

    /**
     * Replace all children by one new child.
     * Intended for "Single child containers" ie a content container that receives a new component on some click in navigation or similar.
     * Rerenders.
     */
    fun replaceContent(newChild: KafffeComponent) {
        removeAllChildren()
        addChild(newChild)
        rerender()
    }

    /**
     * Remove all children without rerendering.
     */
    fun removeAllChildren() {
        while (!children.isEmpty()) {
            removeChild(children[0])
        }
    }

    /**
     * Used to create a delegate property that cuase this component to rerender on set.
     *
     * Example
     * '''
     * class Label(text: String) : KafffeComponent() {
     *   var text: String by rerenderOnChange(text)
     * '''
     */
    fun <Value> rerenderOnChange(value: Value) = RerenderDelegate<Value>(this, value)

    companion object {
        val htmlStart = KafffeHtml.start

        fun ofHtml(htmlProvider: () -> HTMLElement): KafffeComponent =
                object : KafffeComponent() {
                    override fun createHtml(): HTMLElement = htmlProvider()
                }

        fun ofKafffeHtml(htmlProvider: KafffeHtmlBase.() -> KafffeHtmlOut): KafffeComponent =
                object : KafffeComponent() {
                    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut = this.htmlProvider()
                }
    }


}
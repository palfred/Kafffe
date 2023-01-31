package kafffe.core

import kafffe.core.modifiers.AttachAwareModifier
import kafffe.core.modifiers.HtmlElementModifier
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

    /**
     * Find a parent of the given type, usually a KafffeComponent of some kind.
     */
    fun <T : Any> parentOfType(aType: KClass<T>): T? {
        val p = parent
        return if (p == null) null
        else if (aType.isInstance(p)) p.unsafeCast<T>()
        else p.parentOfType(aType)
    }

    /**
     * modifiers can be any mofiger, ie HtmlElementModifier or behaviors, they my be aware of lifecycle like AttachAwareModifier.
     * HtmlElementModifiers are called after HTML is generated.
     * AttachAwareModifiers are called when the component is attached or detached, which can be used to attach or detach listeners on other object.
     */
    val modifiers = mutableListOf<Any>()
    inline fun <reified T> modifiersTyped(): List<T> = modifiers.filter { it is T }.map { it as T}
    private fun attachAwareHtmlModifiers() =
        modifiersTyped<HtmlElementModifier>().filter { it is AttachAwareModifier }.map { it as AttachAwareModifier }

    private var _html: HTMLElement? = null

    var html: HTMLElement
        set(newValue) {
            val oldValue = _html;
            if (oldValue != null) {
                // Call detach on HTMLModifiers to the current
                attachAwareHtmlModifiers().reversed().forEach { it.detach(this) }
                _html = modifyHtml(newValue)
                val parentNode = oldValue.parentNode
                if (parentNode != null) {
                    parentNode.replaceChild(_html!!, oldValue)
                }
            } else {
                _html = if (newValue != null)  modifyHtml(newValue) else null
            }
        }
        get() {
            _html = _html ?: modifyHtml(createHtml())
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
//            console.log(this::class.simpleName + " HTMLModifiers: "  + modifiersTyped<HtmlElementModifier>().size + " here of  AttachAwareModifier:" + modifiersTyped<HtmlElementModifier>().filter { it is AttachAwareModifier }.size)
            // Call detach on HTMLModifiers to the current
            attachAwareHtmlModifiers().reversed().forEach { it.detach(this) }
            _html = null
        }
    }



    /**
     * Marks this and all children as not rendered (forget of previous rendered HTML)
     */
    fun setNotRenderedRecursive() {
        if (isRendered) {
            setNotRendered()
            for (child in children) {
                child.setNotRenderedRecursive()
            }
        }
    }


    protected open fun modifyHtml(element: HTMLElement): HTMLElement {
        modifiersTyped<HtmlElementModifier>().forEach { it.modify(element) }
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
            html = createHtml()
        }
    }


    fun rerenderRecursive() {
        if (isRendered) {
            for (child in children) {
                child.setNotRenderedRecursive()
            }
            html = createHtml()
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
    open fun attach() {
        modifiersTyped<AttachAwareModifier>().forEach { it.attach(this) }
    }

    /**
     * This components is being detached from a component hierarchy.
     * Override this to remove listeners to models and other components setup in attach.
     */
    open fun detach() {
        modifiersTyped<AttachAwareModifier>().reversed().forEach { it.detach(this) }
    }

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
     * Do some operation with this component hierarchy detached
     */
    fun doDetached(block: KafffeComponent.() -> Unit) {
        try {
            detachHierarchy()
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
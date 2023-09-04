package kafffe.core.modifiers

import kafffe.core.KafffeComponent

/**
 * Modifier that is called when a component is attached and detached
 */
interface AttachAwareModifier {
    fun attach(component: KafffeComponent)
    fun detach(component: KafffeComponent)

    companion object {
        class Functional(val onAttach: KafffeComponentConsumer = {}, val onDetach: KafffeComponentConsumer = {}) : AttachAwareModifier {
            override fun attach(component: KafffeComponent) = onAttach(component)
            override fun detach(component: KafffeComponent) = onDetach(component)
        }

        fun create(onAttach: KafffeComponentConsumer = {},  onDetach: KafffeComponentConsumer= {}): AttachAwareModifier = Functional(onAttach, onDetach)

        fun KafffeComponent.attachAwareModifier(onAttach: KafffeComponentConsumer= {},  onDetach: KafffeComponentConsumer = {}): AttachAwareModifier {
            val attachModifier = create(onAttach, onDetach)
            this.modifiers.add(attachModifier)
            return attachModifier
        }
    }
}


package kafffe.core.modifiers

import kafffe.core.KafffeComponent

/**
 * Modifier that is called when a component is attached and detached
 */
interface AttachAwareModifier {
    fun attach()
    fun detach()

    companion object {
        class Functional(val onAttach: () -> Unit = {}, val onDetach: () -> Unit = {}) : AttachAwareModifier {
            override fun attach() = onAttach()
            override fun detach() = onDetach()
        }

        fun create(onAttach: () -> Unit = {},  onDetach: () -> Unit= {}): AttachAwareModifier = Functional(onAttach, onDetach)

        fun KafffeComponent.attachAwareModifier(onAttach: () -> Unit= {},  onDetach: () -> Unit= {}): AttachAwareModifier {
            val attachModifier = create(onAttach, onDetach)
            this.modifiers.add(attachModifier)
            return attachModifier
        }
    }
}


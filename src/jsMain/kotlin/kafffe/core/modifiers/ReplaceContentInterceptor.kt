package kafffe.core.modifiers

/**
 * Implement this to be able to inercept a replaceContent on the praent KafffeComponent.
 * The function should call replace, when the replacement should occur, this could be after a Confirm Dialog
 */
interface ReplaceContentInterceptor {

    /**
     * See interface documentation.
     * @sample
     * ```kotlin
     *    override   fun interceptReplaceContent(replace: () -> Unit) {
     *         if (hasUnsavedChanges()) {
     *             Modal.Companion.confirm(Model.of("TODO have changes"), Model.of("Have unsaved changes proceed to other view"), replace)
     *         } else {
     *             replace()
     *         }
     *     }
     * ```
     */
    fun interceptReplaceContent(replace: () -> Unit) { replace() }
}
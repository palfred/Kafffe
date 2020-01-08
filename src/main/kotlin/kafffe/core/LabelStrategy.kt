package kafffe.core

import kafffe.messages.Messages

/**
 * Label Strategy is used to convert a key to a text model, this could be used to do L12N
 */
interface LabelStrategy {
    fun label(key: String): Model<String>
}

class CamelCaseWordsStrategy : LabelStrategy {

    override fun label(key: String): Model<String> {
        val words: List<String> = "[A-Z\\d]".toRegex().replace(key, { mr -> " ${mr.value}" }).split(' ')
        val text = words.map { s -> s.capitalize() }.joinToString(" ")
        return Model.of(text.trim());
    }

}

class MessagesStrategy<T : Messages>(val prefix: String? = null, val messagesRetriever: () -> T) : LabelStrategy {
    override fun label(key: String): Model<String> {
        return FunctionalModel<String>(getData = fun(): String {
            if (prefix != null) {
                val x = messagesRetriever().asDynamic()["$prefix$key"]
                if (x is String) return x
            }
            val x = messagesRetriever().asDynamic()[key]
            if (x is String) return x
            return CamelCaseWordsStrategy().label(key).data
        })
    }
}
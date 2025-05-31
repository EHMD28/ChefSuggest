package chefsuggest.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Filter(
    var tags: List<String> = listOf(),
    var prepTime: PrepTimeBucket = PrepTimeBucket.NONE,
    var lastUsed: Int = 0,
    var isLocked: Boolean = false,
) {
    override fun toString() : String {
        val json =  Json { encodeDefaults = true }
        return json.encodeToString(this)
    }
}

enum class PrepTimeBucket {
    QUICK,
    MEDIUM,
    LONG,
    NONE,
}

package chefsuggest.core

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Meal(
    var name: String = "",
    var tags: List<String> = listOf(),
    var prepTime: Int = 0,
    var lastUsed: LocalDate
) {
    override fun toString() : String {
        return Json.encodeToString(this)
    }
}

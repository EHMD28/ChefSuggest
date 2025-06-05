package chefsuggest.core

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Meal(
    val name: String,
    val tags: List<String>,
    val prepTime: Int,
    val lastUsed: LocalDate
) {
    override fun toString() : String {
        return Json.encodeToString(this)
    }
}

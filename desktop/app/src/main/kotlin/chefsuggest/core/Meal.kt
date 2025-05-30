package chefsuggest.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Meal(
    @SerialName("name") val name: String,
    @SerialName("tags") val tags: List<String>,
    @SerialName("ingredients") val ingredients: List<String>,
    @SerialName("prep-time") val prepTime: Int,
    @SerialName("last-used") val lastUsed: String
) {
    override fun toString() : String {
        return Json.encodeToString(this)
    }
}

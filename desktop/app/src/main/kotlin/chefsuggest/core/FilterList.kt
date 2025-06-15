package chefsuggest.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class MealConfiguration(val name: String, val filter: Filter)

@Serializable
data class FilterList(val configurations: List<MealConfiguration>) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}

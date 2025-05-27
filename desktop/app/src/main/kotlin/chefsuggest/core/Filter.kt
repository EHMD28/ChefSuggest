package chefsuggest.core

import java.time.LocalDate

data class Filter(
    var tags: List<String> = listOf(),
    var ingredients: List<String> = listOf(),
    var prepTime: NumRange? = null,
    var lastUsed: UInt = 0u,
) {
//    fun setTags(tags: List<String>) {
//        this.tags = tags.toMutableList()
//    }

//    fun setIngredients(ingredients: List<String>) {
//        this.ingredients = ingredients
//    }

    // Sets preparation time filter
    fun setPrepTimeRange(start: UInt, end: UInt) {
        this.prepTime = NumRange(start, end)
    }

    // Sets how many days should have passed since the meal was last used.
    fun setLastUsedRange(days: Int) {
        TODO()
    }

    // Applies all filters to meal list, returning the result.
    fun apply(mealList: MealList) : MealList {
        TODO()
    }
}

data class NumRange(val start: UInt, val end: UInt)

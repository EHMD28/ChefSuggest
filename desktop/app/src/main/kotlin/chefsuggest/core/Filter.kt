package chefsuggest.core

import java.time.LocalDate

class Filter(
    private val tags: MutableList<String> = mutableListOf(),
    private val ingredients: MutableList<String> = mutableListOf(),
    private var prepTime: NumRange? = null,
    private var lastUsed: UInt = 0u,
) {
    // Adds tag to filter.
    fun addTag(tag: String) {
        this.tags.add(tag)
    }

    // Adds ingredient to filter
    fun addIngredient(ingredient: String) {
        this.ingredients.add(ingredient)
    }

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

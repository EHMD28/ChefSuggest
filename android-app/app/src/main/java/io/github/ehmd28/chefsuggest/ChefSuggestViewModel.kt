package io.github.ehmd28.chefsuggest

import androidx.lifecycle.ViewModel
import com.github.kittinunf.fuel.Fuel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChefSuggestUiState(
    /**
     * The list of all possible meal names the generator can choose from.
     */
    val allMeals: List<String> = emptyList(),
    /**
     * The list of meal cards being currently displayed by the user interface. `null` means a meal
     * slot that hasn't been filled yet.
     */
    var selectedMeals: MutableList<MealCardData?> = mutableListOf()
) {
    val numSelectedMeals: Int
        get() = this.selectedMeals.size
}

class ChefSuggestViewModel : ViewModel() {
    private val internalState = MutableStateFlow(ChefSuggestUiState())
    val uiState = internalState.asStateFlow()

    init {
        updateAllMealsFromRemote()
        updateNumMeals(GeneratorConstants.DEFAULT_NUM_MEALS)
    }

    /**
     * Fetches the list of meals from a statically hosted Google Sheets file.
     */
    fun updateAllMealsFromRemote() {
        fun updateInternalState(mealNames: List<String>) {
            internalState.update { currentState ->
                currentState.copy(
                    allMeals = mealNames
                )
            }
        }
        Fuel.get(GeneratorConstants.MEALS_LIST_TSV_URL).response { _, _, result ->
            val (bytes, error) = result
            if (error == null && bytes != null) {
                val bytesAsStr = String(bytes)
                val mealNames = parseMealNamesFromTsv(bytesAsStr)
                updateInternalState(mealNames)
            }
        }
    }

    /**
     * Parses the meal names from a TSV file which is statically hosted using Google Sheets. Since
     * there is currently only one column, it's find to parse it like a normal list.
     */
    private fun parseMealNamesFromTsv(content: String): List<String> {
        // Purpose of the `drop(1)` call is to remove the header row.
        return content.split("\n").drop(1).map { it.trim() }
    }

    fun generateSelectedMealsFile(): String {
        val selectedMealNames = internalState.value.selectedMeals.filterNotNull().map { it.name }
        val content = selectedMealNames.joinToString("\n")
        return content
    }

    fun generatePrettySelectedMealsFile(): String {
        val selectedMealNames = internalState.value.selectedMeals
            .filterNotNull()
            .mapIndexed { index, data ->  "${index + 1}. ${data.name}" }
        val content = selectedMealNames.joinToString("\n")
        return content
    }

    fun updateNumMeals(n: Int) {
        val selected = internalState.value.selectedMeals.toMutableList()
        while (selected.size < n) { selected.add(null) }
        while (selected.size > n) { selected.removeAt(selected.lastIndex) }
        internalState.update { currentState ->
            currentState.copy(
                selectedMeals = selected
            )
        }
    }

    fun toggleLockAtIndex(idx: Int) {
        internalState.update { currentState ->
            val meals = currentState.selectedMeals.toMutableList()
            val meal = meals[idx]
            if (meal != null) {
                meals[idx] = meal.copy(isLocked = !meal.isLocked)
            }
            currentState.copy(
                selectedMeals = meals
            )
        }
    }

    fun generateMeals() {
        val selected = internalState.value.selectedMeals.toMutableList()
        val selectedMealNames = selected.map { it?.name ?: "" }
        val allMeals = internalState.value.allMeals
        val mealNames = allMeals.filter { !selectedMealNames.contains(it) }.shuffled()
        var mealNamesIndex = 0
        for ((index, mealData) in selected.withIndex()) {
            val mealName = mealNames.getOrElse(mealNamesIndex) { "No Meal Found" }
            if (mealData == null) {
                selected[index] = MealCardData(mealName)
                mealNamesIndex++
            } else if (!mealData.isLocked) {
                selected[index] = mealData.copy(name = mealName)
                mealNamesIndex++
            }
        }
        internalState.update { currentState ->
            currentState.copy(
                selectedMeals = selected
            )
        }
    }
}

package io.github.ehmd28.chefsuggest

import androidx.lifecycle.ViewModel
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
        updatedAllMealsList()
        updateNumMeals(GeneratorConstants.DEFAULT_NUM_MEALS)
    }

    /**
     * Fetches the list of meals from a statically hosted Google Sheets file.
     *
     * TODO: Implement HTTP fetch
     */
    fun updatedAllMealsList() {
        val mealNames = listOf(
            "Macaroni and Cheese",
            "Chocolate Cake",
            "BBQ Chicken"
        )
        internalState.update { currentState ->
            currentState.copy(
                allMeals = mealNames
            )
        }
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

    fun generateMeals() {
        TODO()
    }
}

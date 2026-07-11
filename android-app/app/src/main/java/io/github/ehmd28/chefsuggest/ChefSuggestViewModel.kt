package io.github.ehmd28.chefsuggest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChefSuggestUiState(
    val mealsList: List<Meal> = emptyList(),
    var selectedMeals: List<Meal> = emptyList()
)

class ChefSuggestViewModel : ViewModel() {
    private val internalState = MutableStateFlow(ChefSuggestUiState())
    val uiState = internalState.asStateFlow()

    init {
        updateMeals()
    }

    /**
     * Fetches the list of meals from a statically hosted Google Sheets file.
     *
     * TODO: Implement HTTP fetch
     */
    fun updateMeals() {
        val mealList = listOf(
            Meal("Macaroni and Cheese", "https://www.southernliving.com/recipes/best-ever-macaroni-and-cheese-recipe"),
            Meal("Chocolate Cake"),
            Meal("BBQ Chicken", "https://www.simplyrecipes.com/recipes/barbecued_chicken_on_the_grill/")
        )
        internalState.update { currentState ->
            currentState.copy(
                mealsList = mealList
            )
        }
    }

    fun setSelectedMeals(meals: List<Meal>) {
        internalState.update { currentState ->
            currentState.copy(
                selectedMeals = meals
            )
        }
    }
}

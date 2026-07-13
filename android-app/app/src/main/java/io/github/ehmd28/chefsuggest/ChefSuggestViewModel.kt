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
        updateAllMealsFromRemote()
        updateNumMeals(GeneratorConstants.DEFAULT_NUM_MEALS)
    }

    /**
     * Fetches the list of meals from a statically hosted Google Sheets file.
     *
     * TODO: Implement HTTP fetch
     */
    fun updateAllMealsFromRemote() {
        val mealNames = listOf(
            "Cheeseburger Macaroni and Vegetable",
            "Cheeseburger Sliders and Fries",
            "Chicken Noodle Soup and Bread",
            "Chicken or Beef Gyros",
            "Chicken Spaghetti (Recipe: The Pioneer Woman)",
            "Chicken Wings, Macaroni & Cheese, and Vegetable",
            "Chicken, Chicken Rice, and Vegetables",
            "Chicken, Corn Pudding, Collard Greens",
            "Chili Cheese Dogs/Fries",
            "Fettuccine Alfredo",
            "Fried Chicken Sandwiches & Fries",
            "Fried Rice (shrimp or chicken)",
            "Grilled Cheese and Tomato Soup",
            "Grilled Cheese Stuffed Burritos",
            "Homemade Breakfast for Dinner",
            "Homemade Pizza and Fries",
            "Homemade Rice Bowls",
            "Hot Dogs and Beans",
            "Jollof Chicken and Rice",
            "Lasagna and Vegetable",
            "Lo Mein (shrimp or chicken)",
            "Meatball Subs",
            "Meatloaf, Mashed Potatoes, and Vegetable",
            "Philly Cheesesteak Sloppy Joes",
            "Quesadillas",
            "Shredded BBQ Chicken and Macaroni and Cheese",
            "Spaghetti with Meat Sauce and a Vegetable",
            "Steak, Baked Potatoes, and Corn",
            "Tacos",
            "Tater Tot Casserole",
            "Turkey Burgers/Fries",
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

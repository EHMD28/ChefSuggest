package chefsuggest.ui.generator

import chefsuggest.core.MealList
import chefsuggest.utils.Palette
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JPanel

/**
 * Selects one meal for each MealFilterPanel, complying with the filter for each panel. If it is impossible to
 * comply with each filter, the user will be notified of the conflict and asked if they want to allow duplicates.
 */
class GenerateMealsButton(mealFiltersPanel: JPanel, mealList: MealList) : JButton() {
    init {
        this.text = "Generate Meals"
        this.font = Palette.getPrimaryFontWithSize(14)
        this.preferredSize = Dimension(150, 50)
        this.isFocusPainted = false
        this.addActionListener { _ ->
            val mealFilters = mealFiltersPanel.components
            if (mealFilters.size > mealList.size()) TODO()
            val chosenMealsNames = mutableListOf<String>()
            for ((index, panel) in mealFilters.withIndex()) {
                val filterPanel = panel as MealFilterPanel
                val filter = filterPanel.filter
                if (!filter.isLocked) {
                    val filteredMeals = mealList.applyFilter(filter)
                    val meal = filteredMeals.getRandomMeal(chosenMealsNames)
                    if ((meal == null) || filteredMeals.isEmpty) {
                        filterPanel.setMealNameTo("No Meal Found")
                        println("No Meal found for #$index")
                    } else {
                        filterPanel.setMealNameTo(meal.name)
                        println("Set meal #$index to ${meal.name}")
                    }
                }
            }
        }
    }
}

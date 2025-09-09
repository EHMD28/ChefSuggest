package chefsuggest.ui.generator.toolbar

import chefsuggest.ui.core.Globals
import chefsuggest.ui.generator.MealFilterPanel
import chefsuggest.utils.Palette
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JPanel

/**
 * Selects one meal for each MealFilterPanel, complying with the filter for each panel. If it is impossible to
 * comply with each filter, the user will be notified of the conflict and asked if they want to allow duplicates.
 */
class GenerateMealsButton(mealFiltersPanel: JPanel) : JButton() {
    init {
        this.text = "Generate Meals"
        this.font = Palette.getPrimaryFontWithSize(14)
        this.preferredSize = Dimension(150, 50)
        this.isFocusPainted = false
        this.addActionListener { _ ->
            val mealFilters = mealFiltersPanel.components.map { it as MealFilterPanel }
            val chosenNames = mealFilters.mapNotNull { if (it.internalFilter.isLocked) it.mealName else null }.toMutableList()
            for ((index, panel) in mealFilters.withIndex()) {
                val filter = panel.internalFilter
                if (!filter.isLocked) {
                    val filteredMeals = Globals.mealsList.applyFilter(filter)
                    val meal = filteredMeals.getRandomMeal(chosenNames)
                    if ((meal == null) || filteredMeals.isEmpty) {
                        panel.mealName = "No Meal Found"
                    } else {
                        chosenNames.add(meal.name)
                        panel.mealName = meal.name
                    }
                }
            }
        }
    }
}

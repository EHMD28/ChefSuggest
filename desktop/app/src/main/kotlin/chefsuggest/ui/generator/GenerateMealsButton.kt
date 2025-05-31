package chefsuggest.ui.generator

import chefsuggest.core.Filter
import chefsuggest.utils.Palette
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JPanel

class GenerateMealsButton(mealFiltersPanel: JPanel) : JButton() {
    init {
        this.text = "Generate Meals"
        this.font = Palette.getPrimaryFontWithSize(14)
        this.preferredSize = Dimension(150, 50)
        this.addActionListener { _ ->
            println("DEBUG - NEW MEAL FILTER = ${Filter()}")
            val mealFilters = mealFiltersPanel.components
            for ((index, panel) in mealFilters.withIndex()) {
                val mealFilterPanel = panel as MealFilterPanel
                println("DEBUG - Meal Filter $index = ${mealFilterPanel.filter}")
            }
        }
    }
}

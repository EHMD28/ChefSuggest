package chefsuggest.ui.editor

import chefsuggest.core.MealList
import chefsuggest.utils.AppPaths
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JPanel

class Toolbar(val mealsContainer: JPanel) : JPanel() {
    private val loadButon = loadButton()
    private val saveButton = saveButton()

    init {
        this.layout = FlowLayout(FlowLayout.TRAILING)
        this.add(loadButon)
        this.add(saveButton)
    }

    private fun loadButton() : JButton {
        val button = JButton("Load Meals")
        return button
    }

    private fun saveButton() : JButton {
        val button = JButton("Save Meals")
        button.addActionListener {
            val meals = mealsContainer.components.map { (it as MealEditorPanel).meal }
            val mealList = MealList.fromMeals(meals)
            mealList.writeToTsv(AppPaths.getMealsPath())
        }
        return button
    }
}
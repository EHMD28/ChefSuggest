package chefsuggest.ui.generator.toolbar

import chefsuggest.core.MealList
import chefsuggest.ui.generator.MealFilterPanel
import chefsuggest.utils.Palette
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

class NumMealsSpinnerContainer(mealListPanel: JPanel, mealList: MealList) : JPanel() {
    val spinner = spinner()

    init {
        val label = JLabel("Number of Meals to Generate: ")
        label.font = Palette.getPrimaryFontWithSize(16)
        spinner.addChangeListener {
            spinner.commitEdit()
            val value = spinner.value as Int
            val numMealSelectors = mealListPanel.components.size
            if (value > numMealSelectors) {
                val mealFilterPanel = MealFilterPanel(numMealSelectors, mealList)
                val height = mealFilterPanel.preferredSize.height
                val width = mealListPanel.width
                mealFilterPanel.maximumSize = Dimension(width, height)
                mealListPanel.add(mealFilterPanel)
            } else if (value < numMealSelectors) {
                mealListPanel.remove(mealListPanel.components.last())
            }
            mealListPanel.revalidate()
            mealListPanel.repaint()
        }
        this.layout = FlowLayout()
        this.preferredSize = Dimension(280, 50)
        this.add(label)
        this.add(spinner)
    }

    private fun spinner() : JSpinner {
        val initValue = 0
        val minValue = 0
        val maxValue = 10
        val step = 1
        val spinnerModel = SpinnerNumberModel(initValue, minValue, maxValue, step)
        val spinner = JSpinner(spinnerModel)
        val spinnerEditor = spinner.editor as JSpinner.DefaultEditor
        spinnerEditor.textField.preferredSize = Dimension(50, 20)
        spinnerEditor.size = Dimension(50, 20)
        spinnerEditor.textField.isEditable = false
        return spinner
    }
}
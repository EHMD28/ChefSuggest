package chefsuggest.ui.generator

import chefsuggest.utils.Palette
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

class NumMealsSpinner(private val mealListPanel: JPanel) : JPanel() {
    init {
        val label = JLabel("Number of Meals to Generate: ")
        label.font = Palette.getPrimaryFontWithSize(16)
        val initValue = 0
        val minValue = 0
        val maxValue = 10
        val step = 1
        val spinnerModel = SpinnerNumberModel(initValue, minValue, maxValue, step)
        val spinner = JSpinner(spinnerModel)
        spinner.addChangeListener {
            spinner.commitEdit()
            val value = spinner.value as Int;
            val numMealSelectors = mealListPanel.components.size
            if (value > numMealSelectors) {
                val mealSelectorPanel = MealSelectorPanel(numMealSelectors)
                val height = mealSelectorPanel.preferredSize.height
                val width = mealListPanel.width
                mealSelectorPanel.maximumSize = Dimension(width, height)
                mealListPanel.add(mealSelectorPanel)
            } else if (value < numMealSelectors) {
                mealListPanel.remove(mealListPanel.components.last())
                mealListPanel.repaint()
            }
            mealListPanel.revalidate()
        }
        val spinnerEditor = spinner.editor as JSpinner.DefaultEditor
        spinnerEditor.textField.preferredSize = Dimension(50, 20)
        spinnerEditor.size = Dimension(50, 20)
        this.background = Color.PINK
        this.layout = FlowLayout()
        this.preferredSize = Dimension(280, 50)
        this.add(label)
        this.add(spinner)
    }
}
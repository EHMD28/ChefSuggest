package chefsuggest.ui.generator

import chefsuggest.utils.Palette
import java.awt.*
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel


class GeneratorPanel : JPanel() {
    private val controlsPanel = newControlsPanel()
    private val listOfMealsPanel = newListOfMealsPanel()

    init {
        this.layout = BorderLayout()
        this.preferredSize = Dimension(800, 500)
        this.background = Color.WHITE
        this.add(controlsPanel, BorderLayout.NORTH)
        this.add(listOfMealsPanel, BorderLayout.CENTER)
    }

    private fun newControlsPanel() : JPanel {
        val panel = JPanel(BorderLayout())
        panel.background = Color.ORANGE
        panel.add(newNumMealsSpinner(), BorderLayout.WEST)
        panel.add(newGenerateMealsButton(), BorderLayout.EAST)
        return panel
    }

    private fun newNumMealsSpinner() : JPanel {
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
            val numMealSelectors = listOfMealsPanel.components.size
            if (value > numMealSelectors) {
                val mealSelectorPanel = MealSelectorPanel()
                val height = mealSelectorPanel.preferredSize.height
                val width = listOfMealsPanel.width
                mealSelectorPanel.maximumSize = Dimension(width, height)
//                mealSelectorPanel.maximumSize.height = mealSelectorPanel.preferredSize.height
//                mealSelectorPanel.maximumSize.width = listOfMealsPanel.width - 10;
                listOfMealsPanel.add(mealSelectorPanel)
            } else if (value < numMealSelectors) {
                listOfMealsPanel.remove(listOfMealsPanel.components.last())
                listOfMealsPanel.repaint()
            }
            listOfMealsPanel.revalidate()
        }
        val spinnerEditor: JSpinner.DefaultEditor = spinner.editor as JSpinner.DefaultEditor
        spinnerEditor.textField.preferredSize = Dimension(50, 20)
        spinnerEditor.size = Dimension(50, 20)
        val panel = JPanel()
        panel.background = Color.PINK
        panel.layout = FlowLayout()
        panel.preferredSize = Dimension(280, 50)
        panel.add(label)
        panel.add(spinner)
        return panel
    }

    private fun newGenerateMealsButton() : JButton {
        val button = JButton("Generate Meals")
        button.font = Palette.getPrimaryFontWithSize(14)
        button.preferredSize = Dimension(150, 50)
        return button
    }

    private fun newListOfMealsPanel() : JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.background = Color.RED
        return panel
    }
}

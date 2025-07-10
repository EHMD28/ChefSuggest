package chefsuggest.ui.generator

import chefsuggest.core.MealList
import chefsuggest.ui.generator.toolbar.GenerateMealsButton
import chefsuggest.ui.generator.toolbar.LoadMealsButton
import chefsuggest.ui.generator.toolbar.NumMealsSpinnerContainer
import chefsuggest.ui.generator.toolbar.SaveMealsButton
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import kotlin.io.path.Path


class GeneratorTab : JPanel() {
    private val listOfMealsPanel = listOfMealsPanel()
    private val internalPanel = listOfMealsPanel.viewport.view as JPanel
    private val spinnerContainer = NumMealsSpinnerContainer(internalPanel)
    private val saveMealsButton = SaveMealsButton(internalPanel)
    private val loadMealsButton = LoadMealsButton(internalPanel, spinnerContainer.spinner)
    private val generateMealsButton = GenerateMealsButton(internalPanel)
    private val controlsPanel = controlsPanel()

    init {
        this.layout = BorderLayout()
        this.preferredSize = Dimension(1500, 500)
        this.background = Color.WHITE
        this.add(controlsPanel, BorderLayout.NORTH)
        this.add(listOfMealsPanel, BorderLayout.CENTER)
    }

    private fun controlsPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        panel.add(spinnerContainer, BorderLayout.WEST)
        val buttonsContainer = JPanel()
        buttonsContainer.add(this.saveMealsButton)
        buttonsContainer.add(this.loadMealsButton)
        buttonsContainer.add(this.generateMealsButton)
        panel.add(buttonsContainer, BorderLayout.EAST)
        return panel
    }

    private fun listOfMealsPanel(): JScrollPane {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        val scrollPane =
            JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        scrollPane.verticalScrollBar.unitIncrement = 16
        return scrollPane
    }
}

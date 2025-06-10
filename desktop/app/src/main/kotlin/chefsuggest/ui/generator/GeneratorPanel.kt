package chefsuggest.ui.generator

import chefsuggest.core.MealList
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import kotlin.io.path.Path


class GeneratorPanel : JPanel() {
    private val mealList = MealList.fromTsv(Path("app/src/test/kotlin/chefsuggest/resources/LoadTest/TestMeals.tsv"))
    private val listOfMealsPanel = newListOfMealsPanel()
    private val controlsPanel = newControlsPanel()

    init {
        this.layout = BorderLayout()
        this.preferredSize = Dimension(1500, 500)
        this.background = Color.WHITE
        this.add(controlsPanel, BorderLayout.NORTH)
        this.add(listOfMealsPanel, BorderLayout.CENTER)
    }

    private fun newControlsPanel(): JPanel {
        val panel = JPanel(BorderLayout())
//        panel.background = Color.ORANGE
        panel.add(NumMealsSpinner(listOfMealsPanel.viewport.view as JPanel, mealList), BorderLayout.WEST)
        panel.add(GenerateMealsButton(listOfMealsPanel.viewport.view as JPanel, mealList), BorderLayout.EAST)
        return panel
    }

    private fun newListOfMealsPanel(): JScrollPane {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
//        panel.background = Color.RED
        val scrollPane =
            JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        scrollPane.verticalScrollBar.unitIncrement = 16
        return scrollPane
    }
}

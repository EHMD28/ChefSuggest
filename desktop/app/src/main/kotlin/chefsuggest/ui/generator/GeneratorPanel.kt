package chefsuggest.ui.generator

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JScrollPane


class GeneratorPanel : JPanel() {
    private val listOfMealsPanel = newListOfMealsPanel()
    private val controlsPanel = newControlsPanel()

    init {
        this.layout = BorderLayout()
        this.preferredSize = Dimension(800, 500)
        this.background = Color.WHITE
//        val scrollPane =
//            JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        this.add(controlsPanel, BorderLayout.NORTH)
        this.add(listOfMealsPanel, BorderLayout.CENTER)
    }

    private fun newControlsPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        panel.background = Color.ORANGE
        panel.add(NumMealsSpinner(listOfMealsPanel.viewport.view as JPanel), BorderLayout.WEST)
        panel.add(GenerateMealsButton(), BorderLayout.EAST)
        return panel
    }

    private fun newListOfMealsPanel(): JScrollPane {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.background = Color.RED
        val scrollPane =
            JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        return scrollPane
    }
}

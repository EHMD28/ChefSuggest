package chefsuggest.ui.editor

import chefsuggest.ui.core.Globals
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.JPanel
import javax.swing.JScrollPane

class EditorTab : JPanel() {
    private val mealsContainer = mealsContainer()
    private val toolbar = Toolbar(mealsContainer.viewport.view as JPanel)

    init {
        this.layout = BorderLayout()
        this.background = Color.WHITE
        this.add(toolbar, BorderLayout.PAGE_START)
        this.add(mealsContainer, BorderLayout.CENTER)
    }

    private fun mealsContainer() : JScrollPane {
        val panel = JPanel()
        panel.layout = MigLayout("wrap 2, insets 10", "[grow][grow]")
        initMealsPanel(panel)
        val scrollPane = JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        scrollPane.verticalScrollBar.unitIncrement = 16
        return scrollPane
    }

    private fun initMealsPanel(panel: JPanel) {
        for (meal in Globals.mealsList.meals()) {
            panel.add(MealEditorPanel(meal), "growx 50")
        }
    }
}

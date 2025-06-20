package chefsuggest.ui.editor

import chefsuggest.core.Meal
import chefsuggest.core.MealList
import chefsuggest.utils.AppPaths
import kotlinx.datetime.LocalDate
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.JPanel
import javax.swing.JScrollPane
import kotlin.io.path.Path

class EditorTab : JPanel() {
    private val mealList = MealList.fromTsv(AppPaths.getMealsPath())
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
        initMealsPanel(panel)
        panel.layout = MigLayout("wrap 2", "[grow][grow]")
        val scrollPane = JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        scrollPane.verticalScrollBar.unitIncrement = 16
        return scrollPane
    }

    private fun initMealsPanel(panel: JPanel) {
        for (meal in mealList.meals()) {
            panel.add(MealEditorPanel(meal), "growx")
        }
    }
}

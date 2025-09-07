package chefsuggest.ui.core

import chefsuggest.core.MealList
import chefsuggest.utils.AppPaths
import chefsuggest.utils.Palette
import java.awt.BorderLayout
import javax.swing.JFrame

object ChefSuggestWindow : JFrame() {
    private fun readResolve(): Any = ChefSuggestWindow

    init {
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.title = "Chef Suggest"
        this.layout = BorderLayout()
        this.contentPane.background = Palette.PRIMARY_BG
        this.add(TabsPanel(), BorderLayout.CENTER)
        this.pack()
        this.isResizable = false
    }
}

object Globals {
    val mealsList = MealList.fromTsv(AppPaths.getMealsPath())
}

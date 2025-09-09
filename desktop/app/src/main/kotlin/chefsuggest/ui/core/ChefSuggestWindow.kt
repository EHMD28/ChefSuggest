package chefsuggest.ui.core

import chefsuggest.core.MealList
import chefsuggest.utils.AppPaths
import chefsuggest.utils.Palette
import java.awt.BorderLayout
import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JFrame

object ChefSuggestWindow : JFrame() {
    private fun readResolve(): Any = ChefSuggestWindow

    init {
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.title = "Chef Suggest"
        this.iconImage = getWindowIcon()
        this.layout = BorderLayout()
        this.contentPane.background = Palette.PRIMARY_BG
        this.add(TabsPanel(), BorderLayout.CENTER)
        this.pack()
    }

    private fun getWindowIcon() : Image {
        val url = ChefSuggestWindow.javaClass.getResource("/ChefSuggestLogo.png")
        val icon = ImageIcon(url)
        return icon.image
    }
}

object Globals {
    val mealsList = MealList.fromTsv(AppPaths.getMealsPath())
}

package chefsuggest.ui.generator

import java.awt.Color
import java.awt.Dimension
import javax.swing.JPanel

class MealSelectorPanel : JPanel() {
    init {
        this.background = Color.CYAN
        this.preferredSize = Dimension(100, 75)
    }
}

package chefsuggest.ui.generator.toolbar

import chefsuggest.utils.Palette
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JPanel

class LoadMealsButton(filtersPanel: JPanel) : JButton() {
    init {
        this.text = "Load Meals Configuration"
        this.font = Palette.getPrimaryFontWithSize(14)
        this.preferredSize = Dimension(200, 50)
        this.isFocusPainted = false
        this.addActionListener {
            TODO()
        }
    }
}

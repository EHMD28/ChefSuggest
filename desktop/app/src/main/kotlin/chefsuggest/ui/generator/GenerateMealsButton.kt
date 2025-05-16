package chefsuggest.ui.generator

import chefsuggest.utils.Palette
import java.awt.Dimension
import javax.swing.JButton

class GenerateMealsButton : JButton() {
    init {
        this.text = "Generate Meals"
        this.font = Palette.getPrimaryFontWithSize(14)
        this.preferredSize = Dimension(150, 50)
    }
}
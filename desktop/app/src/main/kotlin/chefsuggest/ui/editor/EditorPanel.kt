package chefsuggest.ui.editor

import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JPanel

class EditorPanel : JPanel() {
    init {
        this.layout = FlowLayout()
        this.preferredSize = Dimension(200, 200)
        this.background = Color.WHITE
    }
}

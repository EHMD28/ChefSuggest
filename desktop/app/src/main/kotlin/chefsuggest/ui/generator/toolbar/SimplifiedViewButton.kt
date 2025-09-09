package chefsuggest.ui.generator.toolbar

import chefsuggest.ui.generator.MealFilterPanel
import chefsuggest.utils.Palette
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities

class SimplifiedViewButton(filtersPanel: JPanel) : JButton() {
    init {
        this.text = "View Meals Simplified"
        this.font = Palette.getPrimaryFontWithSize(14)
        this.preferredSize = Dimension(200, 50)
        this.isFocusPainted = false
        this.addActionListener {
            val frame = SwingUtilities.getWindowAncestor(this);
            val names = filtersPanel.components.map { (it as MealFilterPanel).mealName }
            if (names.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "There must be at least one meal.", "Error", JOptionPane.ERROR_MESSAGE)
            } else {
                val dialog = JDialog(frame)
                val panel = JPanel()
                panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
                panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
                names.forEachIndexed { i, name ->
                    val label = JLabel(name)
                    label.font = Palette.getPrimaryFontWithSize(16)
                    panel.add(label)
                }
                dialog.add(panel)
                dialog.pack()
                dialog.isModal = true
                dialog.isVisible = true
            }
        }
    }
}
package chefsuggest.ui.core

import chefsuggest.ui.editor.EditorTab
import chefsuggest.ui.generator.GeneratorTab
import chefsuggest.utils.Palette
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.border.EmptyBorder

class TabsPanel : JPanel() {
    init {
        val generatorPanel = GeneratorTab()
        val editorPanel = EditorTab()
        val tabbedPane = JTabbedPane()
        tabbedPane.addTab("Meal Generator", generatorPanel)
        tabbedPane.addTab("Meal Editor", editorPanel)
        tabbedPane.background = Palette.SECONDARY_BG
        tabbedPane.font = Palette.getPrimaryFontWithSize(18)
        tabbedPane.foreground = Color.BLACK
        tabbedPane.tabLayoutPolicy = JTabbedPane.SCROLL_TAB_LAYOUT
        this.border = EmptyBorder(0, 10, 10, 10)
        this.layout = BorderLayout()
        this.add(tabbedPane, BorderLayout.CENTER)
        this.background = Palette.PRIMARY_BG
    }
}

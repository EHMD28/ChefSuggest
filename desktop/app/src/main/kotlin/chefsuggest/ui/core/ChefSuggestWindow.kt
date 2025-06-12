package chefsuggest.ui.core

import chefsuggest.utils.Palette
import java.awt.BorderLayout
import javax.swing.JFrame

class ChefSuggestWindow {
    companion object {
        private val instance = JFrame()

        init {
            instance.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            instance.title = "Chef Suggest"
            instance.layout = BorderLayout()
            instance.contentPane.background = Palette.PRIMARY_BG
//            instance.jMenuBar = ChefSuggestMenubar()
            instance.add(TabsPanel(), BorderLayout.CENTER)
            instance.pack()
            instance.isVisible = true
        }

        fun getInstance(): JFrame {
            return instance;
        }
    }
}

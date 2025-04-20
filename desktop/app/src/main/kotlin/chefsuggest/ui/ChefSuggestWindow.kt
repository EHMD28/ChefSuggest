package chefsuggest.ui

import chefsuggest.utils.Palette
import java.awt.BorderLayout
import javax.swing.JFrame

class ChefSuggestWindow {
    companion object {
        private val instance = JFrame()

        init {
            instance.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            instance.title = "Chef Suggest"
//            instance.size = Dimension(500, 500)
            instance.layout = BorderLayout()
            this.initUi()
            instance.isVisible = true
        }

        private fun initUi() {
            instance.contentPane.background = Palette.PRIMARY_BG
            instance.add(TabsPanel(), BorderLayout.CENTER)
            instance.pack()
        }

        fun getInstance(): JFrame {
            return instance;
        }
    }
}

package chefsuggest.ui

import java.awt.Dimension
import javax.swing.JFrame

class ChefSuggestWindow {
    companion object {
        private val instance = JFrame()

        init {
            instance.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            instance.title = "Chef Suggest"
            instance.size = Dimension(500, 500)
        }

        fun getInstance(): JFrame {
            return instance;
        }

        fun start() {
            instance.isVisible = true
        }
    }
}

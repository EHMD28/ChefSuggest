package chefsuggest.ui.core

import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class ChefSuggestMenubar : JMenuBar() {
    init {
        val generatorMenu = generatorMenu()
        val editorMenu = editorMenu()
        this.add(generatorMenu)
        this.add(editorMenu)
    }

    private fun generatorMenu() : JMenu {
        val generatorMenu = JMenu("Generator")
//        generatorMenu.mnemonic = KeyEvent.VK_G
        return generatorMenu
    }

    private fun editorMenu() : JMenu {
        val menu = JMenu("Editor")
//        menu.mnemonic = KeyEvent.VK_E
        val loadMealsItem = JMenuItem("Load Meals")
        val saveMealsItem = JMenuItem("Save Meals")
        menu.add(loadMealsItem)
        menu.add(saveMealsItem)
        return menu
    }
}
/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package chefsuggest

import chefsuggest.ui.core.ChefSuggestWindow
import javax.swing.SwingUtilities

class App {
}

fun main() {
    SwingUtilities.invokeLater {
        ChefSuggestWindow.getInstance()
    }
}

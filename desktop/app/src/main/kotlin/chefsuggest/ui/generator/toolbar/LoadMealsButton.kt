package chefsuggest.ui.generator.toolbar

import chefsuggest.core.FilterList
import chefsuggest.core.MealList
import chefsuggest.ui.generator.MealFilterPanel
import chefsuggest.utils.AppPaths
import chefsuggest.utils.Palette
import kotlinx.serialization.json.Json
import java.awt.Dimension
import java.nio.file.Path
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SwingUtilities
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.readText

class LoadMealsButton(filtersPanel: JPanel, mealList: MealList, spinner: JSpinner) : JButton() {
    init {
        this.text = "Load Meals Configuration"
        this.font = Palette.getPrimaryFontWithSize(14)
        this.preferredSize = Dimension(200, 50)
        this.isFocusPainted = false
        this.addActionListener {
            spinner.value = 0
            filtersPanel.removeAll()
            filtersPanel.revalidate()
            filtersPanel.repaint()
            val filePath = this.getConfigPath()
            val text = filePath.readText()
            val data = Json.decodeFromString<FilterList>(text)
            val numPanels = data.configurations.mapIndexed { index, config ->
                val panel = MealFilterPanel(index, mealList)
                filtersPanel.add(panel)
                panel.setMealNameTo(config.name)
                panel.setFilterTo(config.filter)
            }.size
            spinner.value = numPanels
        }
    }

    private fun getConfigPath() : Path {
        val configsPath = AppPaths.getConfigsPath()
        val files = configsPath.listDirectoryEntries()
        val names = files.map { it.name.replace(".config.json", "") }
        val parentFrame = SwingUtilities.getWindowAncestor(this) as JFrame
        val input = JOptionPane.showInputDialog(parentFrame,
            "Which configuration would you like to load?",
            "Choose Configuration",
            JOptionPane.QUESTION_MESSAGE,
            null,
            names.toTypedArray(),
            names.first()
        )
        return configsPath.resolve("$input.config.json")
    }
}

package chefsuggest.ui.generator.toolbar

import chefsuggest.core.FilterList
import chefsuggest.core.MealConfiguration
import chefsuggest.ui.generator.DialogUtilities
import chefsuggest.ui.generator.MealFilterPanel
import chefsuggest.utils.AppPaths
import chefsuggest.utils.Palette
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createFile
import kotlin.io.path.exists

class SaveMealsButton(filtersPanel: JPanel) : JButton() {
    init {
        this.text = "Save Meals Configuration"
        this.font = Palette.getPrimaryFontWithSize(14)
        this.preferredSize = Dimension(200, 50)
        this.isFocusPainted = false
        this.addActionListener {
            val panels = filtersPanel.components.map { it as MealFilterPanel }
            val configs = panels.map { MealConfiguration(it.mealName, it.filter) }
            val filterList = FilterList(configs)
            val dirPath = AppPaths.getConfigsPath()
            // TODO: Add dialog prompting user to name saved configuration.
            val frame = SwingUtilities.getWindowAncestor(this) as JFrame
            val name = DialogUtilities.getSaveMealsName(frame)
            val filePath = dirPath.resolve("${name}.config.json")
            if (!filePath.exists()) filePath.createFile()
            filePath.bufferedWriter().use { file ->
                file.write(filterList.toString())
            }
        }
    }
}

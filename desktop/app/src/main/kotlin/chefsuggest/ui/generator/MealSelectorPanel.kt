package chefsuggest.ui.generator

import chefsuggest.utils.Palette
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

data class MealSelectorPanel(val index: Int) : JPanel() {
    private val label = label()
    private val tagsFilter = tagsFilter()
    private val ingredientsFilter = ingredientsFilter()
    private val prepTimeFilter = prepTimeFilter()
    private val lastUsedFilter = lastUsedFilter()
    private val filtersContainer = filtersContainer()

    init {
        // TODO: Add scrolling.
        this.layout = BorderLayout()
        this.background = Color.WHITE
        val border = BorderFactory.createLineBorder(Color.BLACK)
        val padding = EmptyBorder(10, 10, 10, 10)
        this.border = CompoundBorder(border, padding)
        this.add(label, BorderLayout.LINE_START)
        this.add(filtersContainer, BorderLayout.LINE_END)
        this.preferredSize = Dimension(100, 75)
    }

    private fun label() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        val label = JLabel("${this.index + 1}. Meal Name")
        label.font = Palette.getPrimaryFontWithSize(16)
        panel.add(label, BorderLayout.CENTER)
        return panel
    }

    private fun tagsFilter() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        val button = JButton("Edit Tags")
        val label = JLabel("Selected Tags: ")
        panel.add(button, BorderLayout.PAGE_START)
        panel.add(label, BorderLayout.PAGE_END)
        return panel
    }

    private fun ingredientsFilter() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        val button = JButton("Edit Ingredients")
        val label = JLabel("Selected Ingredients: ")
        panel.add(button, BorderLayout.PAGE_START)
        panel.add(label, BorderLayout.PAGE_END)
        return panel
    }

    private fun prepTimeFilter() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        val label = JLabel("Prep Time Filter:")
        val options = arrayOf(
            "Short (less than 30 minutes)",
            "Medium (between 30 minutes and 1 hour)",
            "Long (more than 1 hour)"
        )
        val dropdown = JComboBox(options)
        dropdown.selectedIndex = 0
        panel.add(label, BorderLayout.PAGE_START)
        panel.add(dropdown, BorderLayout.PAGE_END)
        return panel
    }

    private fun lastUsedFilter() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        val label = JLabel("Days Since Last Used: ")
        val spinner = lastUsedFilterSpinner()
        panel.add(label, BorderLayout.PAGE_START)
        panel.add(spinner, BorderLayout.PAGE_END)
        return panel
    }

    private fun lastUsedFilterSpinner() : JSpinner {
        val initValue = 0
        val minValue = 0
        val maxValue = 365
        val step = 1
        val spinnerModel = SpinnerNumberModel(initValue, minValue, maxValue, step)
        val spinner = JSpinner(spinnerModel)
//        spinner.addChangeListener {
//            spinner.commitEdit()
//        }
        val spinnerEditor = spinner.editor as JSpinner.DefaultEditor
        spinnerEditor.textField.preferredSize = Dimension(50, 20)
        spinnerEditor.size = Dimension(50, 20)
        return spinner
    }

    private fun filtersContainer() : JPanel {
        val panel = JPanel()
//        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.layout = FlowLayout(FlowLayout.LEADING, 20, 10)
        panel.add(tagsFilter)
        panel.add(ingredientsFilter)
        panel.add(prepTimeFilter)
        panel.add(lastUsedFilter)
        return panel
    }
}

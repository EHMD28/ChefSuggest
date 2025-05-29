package chefsuggest.ui.generator

import chefsuggest.core.Filter
import chefsuggest.utils.Palette
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

data class MealSelectorPanel(val index: Int) : JPanel() {
    // GUI Components
    private val label = label()
    private val lockButton = lockButton()
    private val tagsFilterButton = tagsFilterButton()
    private val selectedTagsTextArea = selectedTagsTextArea()
    private val tagsFilterContainer = tagsFilterContainer()
//    private val ingredientsFilterButton = ingredientsFilterButton()
//    private val selectedIngredientsTextArea = ingredientsFilterTextArea()
//    private val ingredientsFilter = ingredientsFilterContainer()
    private val prepTimeFilter = prepTimeFilter()
    private val lastUsedFilter = lastUsedFilter()
    private val filtersContainer = filtersContainer()
    // Functionality Components
    private var isLocked = false
    // TODO: add filter functionality.
    private val filter = Filter()

    init {
        this.layout = BorderLayout()
        this.background = Color.WHITE
        val border = BorderFactory.createLineBorder(Color.BLACK)
        val padding = EmptyBorder(10, 10, 10, 10)
        this.border = CompoundBorder(border, padding)
        this.add(label, BorderLayout.LINE_START)
        this.add(lockButton, BorderLayout.CENTER)
        this.add(filtersContainer, BorderLayout.LINE_END)
        // TODO: Add appropriate minimum and maximum size.
        // ! I HATE BOX_LAYOUT!!!!!!!1!1!!!!1
        this.minimumSize = Dimension(100, 75)
    }

    private fun label() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = BorderLayout()
        val label = JLabel("${this.index + 1}. Meal Name")
        label.font = Palette.getPrimaryFontWithSize(16)
        panel.add(label, BorderLayout.CENTER)
        return panel
    }

    private fun lockButton() : JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.isOpaque = false
        val button = JToggleButton()
        button.isFocusPainted = false
        button.preferredSize = Dimension(150, 40)
        button.text = "Unlocked"
        button.addItemListener { event ->
            if (event.stateChange == ItemEvent.SELECTED) {
                button.text = "Locked"
                this.isLocked = true
            } else if (event.stateChange == ItemEvent.DESELECTED) {
                button.text = "Unlocked"
                this.isLocked = false
            }
        }
        button.alignmentX = Component.CENTER_ALIGNMENT
        panel.add(Box.createVerticalGlue())
        panel.add(button)
        panel.add(Box.createVerticalGlue())
        return panel
    }

    private fun tagsFilterButton() : JButton {
        val button = JButton("Edit Tags")
        button.isFocusPainted = false
        // <TEMP>
        val tempTags = listOf("Tag 1", "Tag 2", "Tag 3", "Tag 4")
        // </TEMP>
        button.addActionListener { _ ->
            val parent = SwingUtilities.getWindowAncestor(this) as JFrame
            val selectedTags = DialogUtilities.selectTags(parent, tempTags)
            if (selectedTags != null) {
                this.filter.tags = selectedTags
                this.selectedTagsTextArea.text = "Selected Tags: ${filter.tags}"
                this.maximumSize = Dimension(this.parent.width, this.preferredSize.height)
                this.revalidate()
                this.parent.revalidate()
                this.parent.repaint()
//                println("DEBUG: PREFERRED SIZE - (${this.preferredSize.width}, ${this.preferredSize.height})")
//                println("DEBUG: ACTUAL SIZE - (${this.width}, ${this.height})")
                println("DEBUG: SELECTED TAGS - $selectedTags")
            }
        }
        return button
    }

    private fun selectedTagsTextArea() : JTextArea {
        val textArea = JTextArea("Selected Tags:")
        textArea.wrapStyleWord = true;
        textArea.lineWrap = true;
        textArea.isOpaque = false;
        textArea.isEditable = false;
        textArea.isFocusable = false;
        return textArea
    }

    private fun tagsFilterContainer() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = BorderLayout()
        panel.add(this.tagsFilterButton, BorderLayout.PAGE_START)
        panel.add(this.selectedTagsTextArea, BorderLayout.PAGE_END)
        return panel
    }

//    private fun ingredientsFilterButton() : JButton {
//        val button = JButton("Edit Ingredients")
//        /// <TEMP>
//        val tempIngredients = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3", "Ingredient 4")
//        /// </TEMP>
//        button.addActionListener { _ ->
//            val parent = SwingUtilities.getWindowAncestor(this) as JFrame
//            val selectedIngredients = DialogUtilities.selectIngredients(parent, tempIngredients)
//            if (selectedIngredients != null) {
//                this.filter.ingredients = selectedIngredients
//                this.selectedIngredientsTextArea.text = "Selected Ingredients: ${filter.ingredients}"
//                this.maximumSize = Dimension(this.parent.width, this.preferredSize.height)
//                this.revalidate()
//                this.parent.revalidate()
//                this.parent.repaint()
////                println("DEBUG: PREFERRED SIZE - (${this.preferredSize.width}, ${this.preferredSize.height})")
////                println("DEBUG: ACTUAL SIZE - (${this.width}, ${this.height})")
//                println("DEBUG: SELECTED TAGS - $selectedIngredients")
//            }
//        }
//        button.isFocusPainted = false
//        return button
//    }
//
//    private fun ingredientsFilterTextArea() : JTextArea {
//        val textArea = JTextArea("Selected Ingredients:")
//        textArea.wrapStyleWord = true;
//        textArea.lineWrap = true;
//        textArea.isOpaque = false;
//        textArea.isEditable = false;
//        textArea.isFocusable = false;
//        return textArea
//    }
//
//    private fun ingredientsFilterContainer() : JPanel {
//        val panel = JPanel()
//        panel.isOpaque = false
//        panel.layout = BorderLayout()
//        val label = JLabel("Selected Ingredients: ")
//        panel.add(this.ingredientsFilterButton, BorderLayout.PAGE_START)
//        panel.add(this.selectedIngredientsTextArea, BorderLayout.PAGE_END)
//        return panel
//    }

    private fun prepTimeFilter() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
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
        panel.isOpaque = false
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
        panel.isOpaque = false
        panel.layout = FlowLayout(FlowLayout.LEADING, 20, 10)
        panel.add(tagsFilterContainer)
//        panel.add(ingredientsFilter)
        panel.add(prepTimeFilter)
        panel.add(lastUsedFilter)
        return panel
    }
}

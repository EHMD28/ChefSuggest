package chefsuggest.ui.generator

import chefsuggest.core.Filter
import chefsuggest.core.MealConfiguration
import chefsuggest.core.MealList
import chefsuggest.core.PrepTimeBucket
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

data class MealFilterPanel(val index: Int, val mealList: MealList) : JPanel() {
    // Getters and Setters
    override fun getMaximumSize(): Dimension {
        return Dimension(
            this.parent.width,
            this.preferredSize.height
        )
    }
    // Variables
    val filter = Filter()
    var mealName = ""
    // Label
    private val label = label()
    private val labelContainer = labelContainer()
    // Lock Button
    private val lockButton = lockButton()
    private val lockButtonContainer = lockButtonContainer()
    // Tags Filter
    private val tagsFilterButton = tagsFilterButton()
    private val selectedTagsTextArea = selectedTagsTextArea()
    private val tagsFilterContainer = tagsFilterContainer()
    // Prep-Time Filter
    private val prepTimeDropdown = prepTimeDropdown()
    private val prepTimeFilterContainer = prepTimeFilterContainer()
    // Last-Used Filter
    private val lastUsedSpinner = lastUsedSpinner()
    private val lastUsedFilterContainer = lastUsedFilterContainer()
    // Container for filters.
    private val filtersContainer = filtersContainer()

    init {
        this.layout = BorderLayout()
        this.background = Color.WHITE
        val border = BorderFactory.createLineBorder(Color.BLACK)
        val padding = EmptyBorder(10, 10, 10, 10)
        this.border = CompoundBorder(border, padding)
        this.add(labelContainer, BorderLayout.LINE_START)
        this.add(lockButtonContainer, BorderLayout.CENTER)
        this.add(filtersContainer, BorderLayout.LINE_END)
        this.minimumSize = Dimension(100, 75)
    }

    private fun label() : JLabel {
        val label = JLabel("${this.index + 1}. Meal Name")
        label.font = Palette.getPrimaryFontWithSize(16)
        return label
    }

    private fun labelContainer() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = BorderLayout()
        panel.add(label, BorderLayout.CENTER)
        return panel
    }

    private fun setFilterLock(isLocked: Boolean) {
        this.filter.isLocked = isLocked
        if (isLocked) {
            this.lockButton.isSelected = true
            this.tagsFilterButton.isEnabled = false
            this.prepTimeDropdown.isEnabled = false
            this.lastUsedSpinner.isEnabled = false
        } else {
            this.lockButton.isSelected = false
            this.tagsFilterButton.isEnabled = true
            this.prepTimeDropdown.isEnabled = true
            this.lastUsedSpinner.isEnabled = true
        }
    }

    private fun setTagsLabel(tags: List<String>) {
        this.filter.tags = tags
        val text = if (tags.isEmpty()) "None" else tags.toString()
        this.selectedTagsTextArea.text = "Selected Tags: $text"
        this.tagsFilterContainer.preferredSize = null
//        this.revalidate()
//        this.parent.revalidate()
//        this.parent.repaint()
    }

    private fun setPrepTime(prepTime: PrepTimeBucket) {
        this.filter.prepTime = prepTime
        this.prepTimeDropdown.selectedIndex = when (prepTime) {
            PrepTimeBucket.NONE -> 0
            PrepTimeBucket.QUICK -> 1
            PrepTimeBucket.MEDIUM -> 2
            PrepTimeBucket.LONG -> 3
        }
    }

    private fun setLastUsed(days: Int) {
        this.filter.lastUsed = days
        this.lastUsedSpinner.value = days
    }

    private fun lockButton() : JToggleButton {
        val button = JToggleButton()
        button.isFocusPainted = false
        button.preferredSize = Dimension(150, 40)
        button.text = "Unlocked"
        button.addItemListener { event ->
            if (event.stateChange == ItemEvent.SELECTED) {
                button.text = "Locked"
                this.setFilterLock(true)
            } else if (event.stateChange == ItemEvent.DESELECTED) {
                button.text = "Unlocked"
                this.setFilterLock(false)
            }
        }
        return button
    }

    private fun lockButtonContainer() : JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.isOpaque = false
        this.lockButton.alignmentX = Component.CENTER_ALIGNMENT
        panel.add(Box.createVerticalGlue())
        panel.add(this.lockButton)
        panel.add(Box.createVerticalGlue())
        return panel
    }

    private fun tagsFilterButton() : JButton {
        val button = JButton("Edit Tags")
        button.isFocusPainted = false
        val tags = mealList.tags()
        button.addActionListener { _ ->
            val parent = SwingUtilities.getWindowAncestor(this) as JFrame
            val result = DialogUtilities.selectTags(parent, tags, this.filter.tags)
            if (!result.isCanceled && result.tags != null) {
                setTagsLabel(result.tags)
                this.revalidate()
                this.parent.revalidate()
                this.parent.repaint()
            }
        }
        return button
    }

    private fun selectedTagsTextArea() : JTextArea {
        val textArea = JTextArea("Selected Tags:")
        textArea.wrapStyleWord = true
        textArea.lineWrap = true
        textArea.isOpaque = false
        textArea.isEditable = false
        textArea.isFocusable = false
        textArea.border = EmptyBorder(10, 0, 0, 0)
        return textArea
    }

    private fun tagsFilterContainer() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = BorderLayout()
        panel.add(this.tagsFilterButton, BorderLayout.PAGE_START)
        panel.add(this.selectedTagsTextArea, BorderLayout.PAGE_END)
        panel.preferredSize = Dimension(250, panel.preferredSize.height)
        return panel
    }

    private fun prepTimeDropdown() : JComboBox<String> {
        val options = arrayOf(
            "No Filter",
            "Short (less than 30 minutes)",
            "Medium (between 30 minutes and 1 hour)",
            "Long (more than 1 hour)"
        )
        val dropdown = JComboBox(options)
        dropdown.selectedIndex = 0
        dropdown.addActionListener { event ->
            val source = event.source as JComboBox<*>
            val selected = source.selectedItem as String
            this.filter.prepTime = when (selected) {
                options[1] -> PrepTimeBucket.QUICK
                options[2] -> PrepTimeBucket.MEDIUM
                options[3] -> PrepTimeBucket.LONG
                else -> PrepTimeBucket.NONE
            }
        }
        return dropdown
    }

    private fun prepTimeFilterContainer() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = BorderLayout()
        val label = JLabel("Prep Time Filter:")
        panel.add(label, BorderLayout.PAGE_START)
        panel.add(this.prepTimeDropdown, BorderLayout.PAGE_END)
        return panel
    }

    private fun lastUsedSpinner() : JSpinner {
        val initValue = 0
        val minValue = 0
        val maxValue = 365
        val step = 1
        val spinnerModel = SpinnerNumberModel(initValue, minValue, maxValue, step)
        val spinner = JSpinner(spinnerModel)
        spinner.addChangeListener {
            spinner.commitEdit()
            this.filter.lastUsed = spinner.value as Int
        }
        val spinnerEditor = spinner.editor as JSpinner.DefaultEditor
        spinnerEditor.textField.preferredSize = Dimension(50, 20)
        spinnerEditor.size = Dimension(50, 20)
        return spinner
    }

    private fun lastUsedFilterContainer() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = BorderLayout()
        val label = JLabel("Days Since Last Used: ")
        panel.add(label, BorderLayout.PAGE_START)
        panel.add(this.lastUsedSpinner, BorderLayout.PAGE_END)
        return panel
    }

    private fun filtersContainer() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = FlowLayout(FlowLayout.LEADING, 20, 10)
        panel.add(tagsFilterContainer)
//        panel.add(ingredientsFilter)
        panel.add(prepTimeFilterContainer)
        panel.add(lastUsedFilterContainer)
        return panel
    }

    fun setMealNameTo(mealName: String) {
        this.mealName = mealName.ifEmpty { "Meal Name" }
        this.label.text = "${index + 1}. $mealName"
    }

    fun setFilterTo(f: Filter) {
        setTagsLabel(f.tags)
        setPrepTime(f.prepTime)
        setLastUsed(f.lastUsed)
        setFilterLock(f.isLocked)
        this.revalidate()
        this.parent.revalidate()
        this.parent.repaint()
    }
}

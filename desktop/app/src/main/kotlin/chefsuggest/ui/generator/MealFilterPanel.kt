package chefsuggest.ui.generator

import chefsuggest.core.Filter
import chefsuggest.core.PrepTimeBucket
import chefsuggest.utils.Palette
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

data class MealFilterPanel(val index: Int) : JPanel() {
    /* This is the best way I found to get BoxLayout to size correctly. */
    override fun getMaximumSize(): Dimension {
        return Dimension(
            this.parent.width,
            this.preferredSize.height
        )
    }

    val internalFilter = Filter()
    var mealName
        get() = labelContainer.label.text ?: ""
        set(value) {
            val text = value.ifEmpty { "Meal Name" }
            labelContainer.label.text = "${index + 1}. $text"
        }
    var isLocked
        get() = internalFilter.isLocked
        set(value) {
            internalFilter.isLocked = value
            if (value) {
                lockButtonContainer.button.isEnabled = true
                tagsFilterContainer.button.isEnabled = false
                prepTimeFilterContainer.dropDown.isEnabled = false
                lastUsedFilterContainer.spinner.isEnabled = false
            } else {
                lockButtonContainer.button.isSelected = false
                tagsFilterContainer.button.isEnabled = true
                prepTimeFilterContainer.dropDown.isEnabled = true
                lastUsedFilterContainer.spinner.isEnabled = true
            }
        }
    var prepTime
        get() = internalFilter.prepTime
        set(value) {
            this.internalFilter.prepTime = value
            prepTimeFilterContainer.dropDown.selectedIndex = when (value) {
                PrepTimeBucket.NONE -> 0
                PrepTimeBucket.QUICK -> 1
                PrepTimeBucket.MEDIUM -> 2
                PrepTimeBucket.LONG -> 3
            }
        }
    var lastUsed
        get() = internalFilter.lastUsed
        set(value) {
            internalFilter.lastUsed = value
            lastUsedFilterContainer.spinner.value = value
        }
    var filter
        get() = this.internalFilter
        set(value) {
            setTagsLabel(value.tags)
            this.prepTime = value.prepTime
            this.lastUsed = value.lastUsed
            this.isLocked = value.isLocked
            this.revalidate()
            this.parent.revalidate()
            this.parent.repaint()
        }

    private val labelContainer = LabelContainer(this)
    private val lockButtonContainer = LockButtonContainer(this)
    private val tagsFilterContainer = TagsFilterContainer(this)
    private val prepTimeFilterContainer = PrepTimeFilterContainer(this)
    private val lastUsedFilterContainer = LastUsedFilterContainer(this)
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

    fun setTagsLabel(tags: List<String>) {
        this.internalFilter.tags = tags
        val text = if (tags.isEmpty()) "None" else tags.toString()
        tagsFilterContainer.textArea.text = "Selected Tags: $text"
        this.tagsFilterContainer.preferredSize = null
    }

    fun filtersContainer() : JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = FlowLayout(FlowLayout.LEADING, 20, 10)
        panel.add(tagsFilterContainer)
        panel.add(prepTimeFilterContainer)
        panel.add(lastUsedFilterContainer)
        return panel
    }
}

private class LabelContainer(val panel: MealFilterPanel) : JPanel() {
    val label = label()

    init {
        this.isOpaque = false
        this.layout = BorderLayout()
        this.add(label, BorderLayout.CENTER)
    }

    private fun label() : JLabel {
        val label = JLabel("${panel.index + 1}. Meal Name")
        label.font = Palette.getPrimaryFontWithSize(16)
        return label
    }
}

private class LockButtonContainer(val panel: MealFilterPanel) : JPanel() {
    val button = toggleButton()

    init {
        this.layout = BoxLayout(this, BoxLayout.Y_AXIS)
        this.isOpaque = false
        button.alignmentX = CENTER_ALIGNMENT
        this.add(Box.createVerticalGlue())
        this.add(button)
        this.add(Box.createVerticalGlue())
    }

    private fun toggleButton() : JToggleButton {
        val button = JToggleButton()
        button.isFocusPainted = false
        button.preferredSize = Dimension(150, 40)
        button.text = "Unlocked"
        button.addItemListener { event ->
            if (event.stateChange == ItemEvent.SELECTED) {
                button.text = "Locked"
                panel.isLocked = true
            } else if (event.stateChange == ItemEvent.DESELECTED) {
                button.text = "Unlocked"
                panel.isLocked = false
            }
        }
        return button
    }
}

private class TagsFilterContainer(val panel: MealFilterPanel) : JPanel() {
    val button = tagsFilterButton()
    val textArea = selectedTagsTextArea()

    init {
        this.isOpaque = false
        this.layout = BorderLayout()
        this.add(button, BorderLayout.PAGE_START)
        this.add(textArea, BorderLayout.PAGE_END)
        this.preferredSize = Dimension(250, this.preferredSize.height)
    }

    private fun tagsFilterButton() : JButton {
        val button = JButton("Edit Tags")
        button.isFocusPainted = false
        button.addActionListener { _ ->
            val parent = SwingUtilities.getWindowAncestor(this) as JFrame
            val result = DialogUtilities.selectTags(parent, panel.internalFilter.tags)
            if (!result.isCanceled && result.tags != null) {
                panel.setTagsLabel(result.tags)
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
}

private class PrepTimeFilterContainer(val panel: MealFilterPanel) : JPanel() {
    val dropDown = prepTimeDropdown()

    init {
        this.isOpaque = false
        this.layout = BorderLayout()
        val label = JLabel("Prep Time Filter:")
        this.add(label, BorderLayout.PAGE_START)
        this.add(dropDown, BorderLayout.PAGE_END)
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
            panel.internalFilter.prepTime = when (selected) {
                options[1] -> PrepTimeBucket.QUICK
                options[2] -> PrepTimeBucket.MEDIUM
                options[3] -> PrepTimeBucket.LONG
                else -> PrepTimeBucket.NONE
            }
        }
        return dropdown
    }
}

private class LastUsedFilterContainer(val panel: MealFilterPanel) : JPanel() {
    val spinner = lastUsedSpinner()

    init {
        this.isOpaque = false
        this.layout = BorderLayout()
        val label = JLabel("Days Since Last Used: ")
        this.add(label, BorderLayout.PAGE_START)
        this.add(spinner, BorderLayout.PAGE_END)
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
            panel.internalFilter.lastUsed = spinner.value as Int
        }
        val spinnerEditor = spinner.editor as JSpinner.DefaultEditor
        spinnerEditor.textField.preferredSize = Dimension(50, 20)
        spinnerEditor.size = Dimension(50, 20)
        return spinner
    }
}

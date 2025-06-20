package chefsuggest.ui.editor

import chefsuggest.core.Meal
import kotlinx.datetime.toJavaLocalDate
import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout
import java.time.format.DateTimeFormatter
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class MealEditorPanel(val meal: Meal) : JPanel() {
    private val label = nameField()
    private val tagsButton = tagsButton()
    private val tagsTextArea = tagsTextArea()
    private val tagsContainer = tagsContainer()
    private val prepTimeSpinner = prepTimeSpinner()
    private val prepTimeContainer = prepTimeContainer()
    private val lastUsedLabel = lastUsedLabel()
    private val removeButton = removeButton()

    init {
        this.layout = FlowLayout(FlowLayout.LEADING, 20, 0)
        val border = BorderFactory.createLineBorder(Color.BLACK)
        val padding = EmptyBorder(10, 10, 10, 10)
        this.border = CompoundBorder(border, padding)
        this.add(label)
        this.add(tagsContainer)
        this.add(prepTimeContainer)
        this.add(lastUsedLabel)
        this.add(removeButton)
    }

    private fun nameField() : JTextField {
        val textField = JTextField(meal.name)
        textField.border = BorderFactory.createEmptyBorder()
        textField.isOpaque = false
        textField.addActionListener {
            this.meal.name = textField.text.trim()
        }
        return textField
    }

    private fun tagsButton() : JButton {
        val tagsButton = JButton("Edit Tags")
        return tagsButton
    }

    private fun tagsTextArea() : JTextArea {
        val textArea = JTextArea("Selected Tags")
        return textArea
    }

    private fun tagsContainer() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(tagsButton, BorderLayout.PAGE_START)
        panel.add(tagsTextArea, BorderLayout.PAGE_END)
        return panel
    }

    private fun prepTimeSpinner() : JSpinner {
        val initialValue = meal.prepTime
        val min = 0
        val max = 100
        val step = 1
        val spinnerModel = SpinnerNumberModel(initialValue, min, max, step)
        val spinner = JSpinner(spinnerModel)
        spinner.addChangeListener {
            spinner.commitEdit()
            this.meal.prepTime = spinner.value as Int
        }
        return spinner
    }

    private fun prepTimeContainer() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        val label = JLabel("Prep Time:")
        panel.add(label, BorderLayout.PAGE_START)
        panel.add(prepTimeSpinner, BorderLayout.PAGE_END)
        return panel
    }

    private fun lastUsedLabel() : JLabel {
        val text = meal.lastUsed.toJavaLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        val lastUsed = JLabel("Last Used: $text")
        return lastUsed
    }

    private fun removeButton() : JButton {
        val button = JButton("X")
        button.background = Color.RED
        button.foreground = Color.WHITE
        return button
    }
}
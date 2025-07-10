package chefsuggest.ui.editor

import chefsuggest.core.Meal
import chefsuggest.ui.core.Globals
import chefsuggest.ui.generator.DialogUtilities
import kotlinx.datetime.toJavaLocalDate
import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.time.format.DateTimeFormatter
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextArea
import javax.swing.SpinnerNumberModel
import javax.swing.SwingUtilities
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class MealEditorPanel(val meal: Meal) : JPanel() {
    private val label = nameField()
    private val tagsContainer = TagsContainer(meal)
    private val prepTimeContainer = PrepTimeContainer(meal)
    private val lastUsedContainer = LastUsedContainer(meal)
    private val removeButton = removeButton()

    init {
        this.layout = FlowLayout(FlowLayout.LEADING, 20, 0)
        val border = BorderFactory.createLineBorder(Color.BLACK)
        val padding = EmptyBorder(10, 10, 10, 10)
        this.border = CompoundBorder(border, padding)
        this.add(label)
        this.add(tagsContainer)
        this.add(prepTimeContainer)
        this.add(lastUsedContainer)
        this.add(removeButton)
    }

    private fun nameField() : JTextArea {
        // TODO: JTextField -> JTextArea to support longer names.
        val rows = 1
        val columns = 10
        val textArea = JTextArea(meal.name, rows, columns)
        textArea.lineWrap = true
        textArea.wrapStyleWord = true
        textArea.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                if (e?.keyCode == KeyEvent.VK_ENTER) {
                    val thisPanel = this@MealEditorPanel
                    thisPanel.requestFocusInWindow()
                    thisPanel.meal.name = textArea.text.trim()
                }
            }
        })
        return textArea
    }

    private fun removeButton() : JButton {
        val button = JButton("X")
        button.addActionListener {
            /* Calling parent.remove(this) means parent can no longer be referenced using this. */
            val parent = this.parent
            parent.remove(this)
            Globals.mealsList.removeMeal(this.meal)
            parent.revalidate()
            parent.repaint()
        }
        button.background = Color.RED
        button.foreground = Color.WHITE
        return button
    }
}

private class TagsContainer(val meal: Meal) : JPanel() {
    private val tagsButton = tagsButton()

    init {
        this.layout = BorderLayout()
        this.add(tagsButton, BorderLayout.CENTER)
    }

    private fun tagsButton() : JButton {
        val tagsButton = JButton("Edit Tags")
        tagsButton.addActionListener {
            val frame = SwingUtilities.getWindowAncestor(this) as JFrame
            DialogUtilities.editTags(frame, this.meal)
        }
        return tagsButton
    }
}

private class PrepTimeContainer(val meal: Meal) : JPanel() {
    private val prepTimeSpinner = prepTimeSpinner()

    init {
        this.layout = BorderLayout()
        val label = JLabel("Prep Time:")
        this.add(label, BorderLayout.PAGE_START)
        this.add(prepTimeSpinner, BorderLayout.PAGE_END)
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
}

private class LastUsedContainer(val meal: Meal) : JPanel() {
    private val label = label()
    private val button = button()

    init {
        this.layout = BorderLayout()
        this.add(label, BorderLayout.PAGE_START)
        this.add(button, BorderLayout.PAGE_END)
    }

    private fun label() : JLabel {
        val text = meal.lastUsed.toJavaLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        val lastUsed = JLabel("Last Used: $text")
        return lastUsed
    }

    private fun button() : JButton {
        val button = JButton("Update Date")
        button.addActionListener {
            val frame = SwingUtilities.getWindowAncestor(this) as JFrame
            val date = DialogUtilities.chooseDate(frame, meal.lastUsed)
            val text = date.toJavaLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            label.text = text
        }
        return button
    }
}

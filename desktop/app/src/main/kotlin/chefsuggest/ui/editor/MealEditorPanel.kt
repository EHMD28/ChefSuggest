package chefsuggest.ui.editor

import chefsuggest.core.Meal
import chefsuggest.core.MealList
import chefsuggest.ui.generator.DialogUtilities
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.todayIn
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
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel
import javax.swing.SwingUtilities
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class MealEditorPanel(val meal: Meal, val mealList: MealList) : JPanel() {
    private val label = nameField()
    private val tagsButton = tagsButton()
    private val tagsContainer = tagsContainer()
    private val prepTimeSpinner = prepTimeSpinner()
    private val prepTimeContainer = prepTimeContainer()
    private val lastUsedLabel = lastUsedLabel()
    private val lastUsedButton = lastUsedButton()
    private val lastUsedContainer = lastUsedContainer()
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

    private fun tagsButton() : JButton {
        val tagsButton = JButton("Edit Tags")
        tagsButton.addActionListener {
            val frame = SwingUtilities.getWindowAncestor(this) as JFrame
            DialogUtilities.editTags(frame, mealList, this.meal)
        }
        return tagsButton
    }

    private fun tagsContainer() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(tagsButton, BorderLayout.CENTER)
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

    private fun lastUsedButton() : JButton {
        val button = JButton("Update Date")
        button.addActionListener {
            val frame = SwingUtilities.getWindowAncestor(this) as JFrame
            val date = DialogUtilities.chooseDate(frame, meal.lastUsed)
            val text = date.toJavaLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            lastUsedLabel.text = text
        }
        return button
    }

    private fun lastUsedContainer() : JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(lastUsedLabel, BorderLayout.PAGE_START)
        panel.add(lastUsedButton, BorderLayout.PAGE_END)
        return panel
    }

    private fun removeButton() : JButton {
        val button = JButton("X")
        button.addActionListener {
            /* Calling parent.remove(this) means parent can no longer be referenced using this. */
            val parent = this.parent
            parent.remove(this)
            mealList.removeMeal(this.meal)
            parent.revalidate()
            parent.repaint()
        }
        button.background = Color.RED
        button.foreground = Color.WHITE
        return button
    }
}
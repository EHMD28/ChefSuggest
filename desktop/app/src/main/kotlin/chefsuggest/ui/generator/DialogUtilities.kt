package chefsuggest.ui.generator

import chefsuggest.core.Filter
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Frame
import javax.swing.*

class DialogUtilities {
    companion object {
        fun selectTags(frame: Frame, tags: List<String>): List<String>? {
            val chosenTags: MutableList<String> = mutableListOf()
            SelectTagsDialog(frame, tags, chosenTags)
            return if (chosenTags.size == 0) {
                null
            } else {
                chosenTags
            }
        }

        fun selectIngredients(frame: Frame, ingredients: List<String>): List<String>? {
            val chosenIngredients: MutableList<String> = mutableListOf()
            SelectIngredientsDialog(frame, ingredients, chosenIngredients)
            return if (chosenIngredients.size == 0) {
                null
            } else {
                chosenIngredients
            }
        }
    }
}

private class SelectTagsDialog(frame: Frame, private val tags: List<String>, private val chosen: MutableList<String>) :
    JDialog(frame) {
    private val tagsContainer = tagsContainer()
    private val buttonsContainer = buttonsContainer()

    init {
        this.defaultCloseOperation = JDialog.DISPOSE_ON_CLOSE
        this.modalityType = ModalityType.APPLICATION_MODAL
        this.title = "Select Tags"
        this.layout = BorderLayout()
        this.add(tagsContainer, BorderLayout.LINE_START)
        this.add(buttonsContainer, BorderLayout.PAGE_END)
        this.pack()
        this.isVisible = true
    }

    private fun tagsContainer(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        for (tag in this.tags) {
            val checkbox = JCheckBox(tag)
            checkbox.isFocusPainted = false
            checkbox.addActionListener { event ->
                val source = event.source as JCheckBox
                if (source.isSelected) {
                    this.chosen.add(tag)
                } else {
                    this.chosen.remove(tag)
                }
            }
            panel.add(checkbox)
        }
        return panel
    }

    private fun buttonsContainer(): JPanel {
        val panel = JPanel()
        val buttonDimension = Dimension(100, 25)
        val applyButton = JButton("Apply")
        applyButton.preferredSize = buttonDimension
        applyButton.isFocusPainted = false
        applyButton.addActionListener { event ->
            if (event.source == applyButton) {
                this.isVisible = false
                this.dispose()
            }
        }
        val cancelButton = JButton("Cancel")
        cancelButton.preferredSize = buttonDimension
        cancelButton.isFocusPainted = false
        cancelButton.addActionListener { event ->
            if (event.source == cancelButton) {
                this.isVisible = false
                this.dispose()
                this.chosen.clear()
            }
        }
        panel.add(applyButton)
        panel.add(cancelButton)
        return panel
    }
}

private class SelectIngredientsDialog(
    frame: Frame,
    private val ingredients: List<String>,
    private val chosen: MutableList<String>
) : JDialog(frame) {
    private val ingredientsContainer = ingredientsContainer()
    private val buttonsContainer = buttonsContainer()

    init {
        this.defaultCloseOperation = JDialog.DISPOSE_ON_CLOSE
        this.modalityType = ModalityType.APPLICATION_MODAL
        this.title = "Select Ingredients"
        this.layout = BorderLayout()
        this.add(ingredientsContainer, BorderLayout.LINE_START)
        this.add(buttonsContainer, BorderLayout.PAGE_END)
        this.pack()
        this.isVisible = true
    }

    private fun ingredientsContainer(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        for (ingredient in this.ingredients) {
            val checkbox = JCheckBox(ingredient)
            checkbox.isFocusPainted = false
            checkbox.addActionListener { event ->
                val source = event.source as JCheckBox
                if (source.isSelected) {
                    this.chosen.add(ingredient)
                } else {
                    this.chosen.remove(ingredient)
                }
            }
            panel.add(checkbox)
        }
        return panel
    }

    private fun buttonsContainer(): JPanel {
        val panel = JPanel()
        val buttonDimension = Dimension(100, 25)
        val applyButton = JButton("Apply")
        applyButton.preferredSize = buttonDimension
        applyButton.isFocusPainted = false
        applyButton.addActionListener { _ ->
            this.isVisible = false
            this.dispose()
        }
        val cancelButton = JButton("Cancel")
        cancelButton.preferredSize = buttonDimension
        cancelButton.isFocusPainted = false
        cancelButton.addActionListener { _ ->
            this.isVisible = false
            this.dispose()
            this.chosen.clear()
        }
        panel.add(applyButton)
        panel.add(cancelButton)
        return panel
    }
}

package chefsuggest.ui.generator

import chefsuggest.core.Meal
import chefsuggest.core.MealList
import chefsuggest.utils.AppPaths
import kotlinx.datetime.LocalDate
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Frame
import java.time.YearMonth
import java.util.Calendar
import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

data class TagsSelectionResult(val tags: List<String>?, val isCanceled: Boolean)

object DialogUtilities {
    fun selectTags(frame: Frame, allTags: List<String>, chosen: List<String>): TagsSelectionResult {
        val chosenTags: MutableList<String> = chosen.toMutableList()
        val dialog = SelectTagsDialog(frame, allTags, chosenTags)
        return TagsSelectionResult(
            tags = if (dialog.isCanceled) null else chosenTags,
            isCanceled = dialog.isCanceled
        )
    }

    fun getSaveMealsName(frame: Frame): String {
        val configsPath = AppPaths.getConfigsPath()
        val usedNames = configsPath.listDirectoryEntries().map { it.name.split(".").first() }
        while (true) {
            val name = JOptionPane.showInputDialog("What name would you like to save this configuration as?")
            if (name.isEmpty() || !name.all { it.isLetterOrDigit() }) JOptionPane.showMessageDialog(
                frame,
                "Invalid name."
            )
            else if (name !in usedNames) return name
            else JOptionPane.showMessageDialog(frame, "Name is already used by another configuration.")
        }
    }

    fun editTags(frame: Frame, mealList: MealList, meal: Meal) {
        EditTagsDialog(frame, mealList.tags(), meal)
    }

    fun chooseDate(frame: Frame, defaultDate: LocalDate): LocalDate {
        val dialog = ChooseDateDialog(frame, defaultDate)
        return if (dialog.wasCanceled) {
            defaultDate
        } else {
            dialog.buildDate()
        }
    }
}

private class SelectTagsDialog(frame: Frame, private val tags: List<String>, private val chosen: MutableList<String>) :
    JDialog(frame) {
    var isCanceled = false
    private val tagsContainer = tagsContainer()
    private val buttonsContainer = buttonsContainer()

    init {
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
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
            checkbox.isSelected = tag in chosen
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
                this.isCanceled = true
            }
        }
        panel.add(applyButton)
        panel.add(cancelButton)
        return panel
    }
}

private var <T> JList<T>.selectedElements: List<T>
    get() = List(model.size) { model.getElementAt(it) }
    set(values) {
        val items = this.selectedElements
        /* indexOf() returns -1 if the value is not found, but it doesn't matter because setSelectedIndices will
        ignore invalid values */
        this.selectedIndices = values.map { items.indexOf(it) }.toIntArray()
    }

private class EditTagsDialog(frame: Frame, private val allTags: List<String>, private val meal: Meal) : JDialog(frame) {
    private val saveExitPanel = saveExitPanel()
    private val tagsListModel = tagsListModel()

    //    private val tagsListSelectionModel = tagsListSelectionModel()
    private val tagsList = tagsList()
    private val tagsScrollPane = tagsScrollPane()
    private val selectedTextField = selectedTextField()
    private val addRemovePanel = addRemovePanel()
    private val editedTags = allTags.toMutableList()

    init {
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.modalityType = ModalityType.APPLICATION_MODAL
        this.title = "Edit List of Tags"
        this.layout = BorderLayout()
        this.add(saveExitPanel, BorderLayout.PAGE_START)
        this.add(tagsScrollPane, BorderLayout.CENTER)
        this.add(addRemovePanel, BorderLayout.PAGE_END)
        this.pack()
        this.isVisible = true
    }

    private fun saveExitPanel(): JPanel {
        val panel = JPanel()
        val saveButton = JButton("Save")
        saveButton.addActionListener { meal.tags = tagsList.selectedValuesList }
        val exitButton = JButton("Exit")
        exitButton.addActionListener { this.dispose() }
        panel.add(saveButton)
        panel.add(exitButton)
        return panel
    }

    private fun tagsListModel(): DefaultListModel<String> {
        return DefaultListModel<String>()
    }

    private fun tagsListSelectionModel(): DefaultListSelectionModel {
        return object : DefaultListSelectionModel() {
            private var gestureStarted = false

            override fun setSelectionInterval(index0: Int, index1: Int) {
                if (!gestureStarted) {
                    if (isSelectedIndex(index0)) {
                        super.removeSelectionInterval(index0, index1)
                    } else {
                        super.addSelectionInterval(index0, index1)
                    }
                }
                gestureStarted = true
            }

            override fun setValueIsAdjusting(isAdjusting: Boolean) {
                if (!isAdjusting) {
                    gestureStarted = false
                }
                super.setValueIsAdjusting(isAdjusting)
            }
        }
    }

    private fun tagsList(): JList<String> {
        val list = JList(tagsListModel)
        list.model = tagsListModel()
        allTags.forEach { (list.model as DefaultListModel<String>).addElement(it) }
        list.selectionModel = tagsListSelectionModel()
        list.selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        list.layoutOrientation = JList.VERTICAL
        list.selectedElements = meal.tags
        list.addListSelectionListener(object : ListSelectionListener {
            var previous = listOf<Int>()
            override fun valueChanged(ev: ListSelectionEvent?) {
                if (ev == null) return
                if (!ev.valueIsAdjusting) {
                    val current = list.selectedIndices.toList()
                    val difference = current - previous
                    if (difference.size == 1) {
                        selectedTextField.text = list.model.getElementAt(difference[0])
                    }
                    previous = current
                }
            }
        })
        return list
    }

    private fun tagsScrollPane(): JScrollPane {
        val scrollPane =
            JScrollPane(tagsList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        return scrollPane
    }

    private fun selectedTextField(): JTextField {
        val textField = JTextField(20)
        return textField
    }

    private fun addRemovePanel(): JPanel {
        val panel = JPanel()
        val removeButton = JButton("Remove")
        val addButton = JButton("Add")
        addButton.addActionListener {
            val model = tagsList.model as DefaultListModel<String>
            val tag = selectedTextField.text
            if (!model.toArray().contains(tag)) {
                model.addElement(tag)
            } else {
                val parent = SwingUtilities.getWindowAncestor(this) as JFrame
                JOptionPane.showMessageDialog(
                    parent,
                    "Tag with that name already exists.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
        removeButton.addActionListener {
            val model = tagsList.model as DefaultListModel<String>
            val tag = selectedTextField.text
            val parent = SwingUtilities.getWindowAncestor(this) as JFrame
            if (model.toArray().contains(tag)) {
                model.removeElement(selectedTextField.text)
                JOptionPane.showMessageDialog(
                    parent,
                    "Successfully deleted '$tag' tag",
                    "Tag Deleted",
                    JOptionPane.INFORMATION_MESSAGE
                )
            } else {
                JOptionPane.showMessageDialog(
                    parent,
                    "Tag with that name does not exist",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
        panel.add(removeButton)
        panel.add(selectedTextField)
        panel.add(addButton)
        return panel
    }
}

private class ChooseDateDialog(frame: Frame, val defaultDate: LocalDate) : JDialog(frame) {
    private val monthDropdown = monthDropdown()
    private val dayDropdown = dayDropdown()
    private val yearDropdown = yearDropDown()
    private val dateContainer = dateContainer()
    private val buttonsContainer = buttonsContainer()
    var wasCanceled = false

    init {
        this.defaultCloseOperation = DISPOSE_ON_CLOSE
        this.modalityType = ModalityType.APPLICATION_MODAL
        this.title = "Choose Date"
//        this.layout = BorderLayout()
        /* Dirty hack to force dropdown to render correct number of days. */
        monthDropdown.selectedIndex = monthDropdown.selectedIndex
        this.add(dateContainer, BorderLayout.PAGE_START)
        this.add(buttonsContainer, BorderLayout.PAGE_END)
        this.pack()
        this.isResizable = false
        this.isVisible = true
    }

    private fun monthDropdown(): JComboBox<String> {
        val months =
            java.time.Month.entries.map { it.toString().lowercase().replaceFirstChar { ch -> ch.uppercase() } }
        val dropdown = JComboBox(months.toTypedArray())
        dropdown.selectedIndex = this.defaultDate.monthNumber - 1
        dropdown.addActionListener {
            val month =
                YearMonth.of(yearDropdown.selectedItem as Int, monthDropdown.selectedIndex + 1)
            val numDays = month.lengthOfMonth()
            val currentLength = dayDropdown.itemCount
            if (numDays > currentLength) {
                val range = currentLength..numDays
                range.forEach { dayDropdown.addItem(it) }
            } else if (numDays < currentLength) {
                val range = currentLength downTo (numDays + 1)
                range.forEach { dayDropdown.removeItemAt(it - 1) }
            }
        }
        return dropdown
    }

    private fun dayDropdown(): JComboBox<Int> {
        val days = (1..31).toList()
        val dropdown = JComboBox(days.toTypedArray())
        dropdown.selectedItem = this.defaultDate.dayOfMonth
        return dropdown
    }

    private fun yearDropDown(): JComboBox<Int> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val startYear = 2000
        val years = (currentYear downTo startYear).toList()
        val dropdown = JComboBox(years.toTypedArray())
        dropdown.selectedItem = defaultDate.year
        return dropdown
    }

    private fun dateContainer() : JPanel {
        val panel = JPanel()
        panel.add(JLabel("Month: "))
        panel.add(monthDropdown)
        panel.add(JLabel("Day: "))
        panel.add(dayDropdown)
        panel.add(JLabel("Year: "))
        panel.add(yearDropdown)
        return panel
    }

    private fun buttonsContainer() : JPanel {
        val layout = FlowLayout()
        layout.vgap = 10
        val panel = JPanel(layout)
        val acceptButton = JButton("Accept")
        val cancelButton = JButton("Cancel")
        panel.add(acceptButton)
        acceptButton.addActionListener { this.dispose() }
        panel.add(cancelButton)
        cancelButton.addActionListener {
            this.wasCanceled = true
            this.dispose()
        }
        return panel
    }

    fun buildDate() : LocalDate {
        val day = dayDropdown.selectedItem as Int
        val month = monthDropdown.selectedIndex + 1
        val year = yearDropdown.selectedItem as Int
        return LocalDate(year, month, day)
    }
}

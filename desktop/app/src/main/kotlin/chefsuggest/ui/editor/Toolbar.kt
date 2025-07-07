package chefsuggest.ui.editor

import chefsuggest.core.Meal
import chefsuggest.core.MealList
import chefsuggest.utils.AppPaths
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities

class Toolbar(val editorsContainer: JPanel) : JPanel() {
    private val newButton = newButton()
    private val loadButon = loadButton()
    private val saveButton = saveButton()
    private var mealList: MealList

    init {
        val meals = editorsContainer.components.map { (it as MealEditorPanel).meal }
        mealList = MealList.fromMeals(meals)
        this.layout = FlowLayout(FlowLayout.TRAILING)
        this.add(newButton)
        this.add(loadButon)
        this.add(saveButton)
    }

    private fun newButton(): JButton {
        val button = JButton("New Meal")
        button.addActionListener {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val meal = Meal(name = "Meal Name", lastUsed = today)
            mealList.addMeal(meal)
            editorsContainer.add(MealEditorPanel(meal, mealList), "growx 50")
            editorsContainer.revalidate()
            editorsContainer.repaint()
        }
        return button
    }

    private fun loadButton(): JButton {
        val button = JButton("Load Meals")
        return button
    }

    private fun saveButton(): JButton {
        val button = JButton("Save Meals")
        button.addActionListener {
            val frame = SwingUtilities.getWindowAncestor(this) as JFrame
            val yes = 1
            val confirmation = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure? This action cannot be undone.",
                "Confirm",
                JOptionPane.YES_NO_OPTION
            );
            if (confirmation == yes) {
                val meals = editorsContainer.components.map { (it as MealEditorPanel).meal }
                val mealList = MealList.fromMeals(meals)
                mealList.writeToTsv(AppPaths.getMealsPath())
            }
        }
        return button
    }
}
package chefsuggest.core

import kotlinx.datetime.LocalDate
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.map
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.readTsv
import org.jetbrains.kotlinx.dataframe.io.writeTsv
import java.nio.file.Path

class MealList private constructor() {
    private val mealList = mutableListOf<Meal>()

    companion object {
        fun fromMeals(meals: List<Meal>): MealList {
            val mealList = MealList()
            for (meal in meals) {
                mealList.addMeal(meal)
            }
            return mealList
        }

        fun fromTsv(path: Path): MealList {
            val df = DataFrame.readTsv(path).cast<Meal>()
            val meals = df.map { row ->
                Meal(
                    name = row["name"] as String,
                    tags = (row["tags"] as String).split(","),
                    prepTime = row["prepTime"] as Int,
                    lastUsed = row["lastUsed"] as LocalDate
                )
            }
            val mealList = fromMeals(meals.sortedBy { it.name })
            return mealList
        }
    }

    private data class MealRow(val name: String, val tags: String, val prepTime: Int, val lastUsed: LocalDate)

    fun writeToTsv(path: Path) {
        val mealRows =
            this.mealList.map { meal -> MealRow(
                name = meal.name,
                tags = meal.tags.joinToString(","),
                prepTime = meal.prepTime,
                lastUsed = meal.lastUsed
            ) }
        val df = mealRows.toDataFrame()
        df.writeTsv(path)
    }

    /**
     * @throws IllegalArgumentException if `Meal`'s name is already in `MealList`
     */
    fun addMeal(meal: Meal) {
//        require(meal.name !in mealNames) { "Meal with that name already exists." }
        this.mealList.add(meal)
        this.mealList.sortBy { it.name }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is MealList) throw IllegalArgumentException("Expected MealList type")
        this.mealList.zip(other.mealList).forEach { (thisMeal, otherMeal) ->
            if (thisMeal != otherMeal) {
                return false
            }
        }
        return true
    }

    override fun toString(): String {
        return mealList.toString()
    }

    override fun hashCode(): Int {
        return mealList.hashCode()
    }
}

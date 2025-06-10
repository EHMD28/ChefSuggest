package chefsuggest.core

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.todayIn
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.map
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.readTsv
import org.jetbrains.kotlinx.dataframe.io.writeTsv
import java.nio.file.Path
import java.time.Period
import java.time.temporal.ChronoUnit

class MealList private constructor() {
    private val mealList = mutableListOf<Meal>()
    private val mealNames
        get() = this.mealList.map { it.name }.sorted()
    val isEmpty
        get() = this.mealList.isEmpty()

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

    fun size() : Int {
        return this.mealList.size
    }

    fun tags() : List<String> {
        return this.mealList.map { it.tags }.flatten().distinct().sorted()
    }

    /**
     * Returns a randomly chosen meal with a name that is not in except parameter. Returns null
     * if no meal is found
     */
    fun getRandomMeal(except: List<String>) : Meal? {
        // If all meals are the same, it is impossible to pick a unique one.
        if (this.mealNames == except) return null
        while (true) {
            val meal = this.mealList.random()
            if (meal.name !in except) {
                return meal
            }
        }
    }

    /**
     * @throws IllegalArgumentException if `Meal`'s name is already in `MealList`
     */
    fun addMeal(meal: Meal) {
//        require(meal.name !in mealNames) { "Meal with that name already exists." }
        this.mealList.add(meal)
        this.mealList.sortBy { it.name }
    }

    fun applyFilter(filter: Filter) : MealList {
        val filtered = this.mealList.filter { meal ->
            val cond1 = filter.tags.isEmpty() || meal.tags.any { it in filter.tags }
            val cond2 = when (filter.prepTime) {
                PrepTimeBucket.QUICK -> meal.prepTime <= 30
                PrepTimeBucket.MEDIUM -> meal.prepTime <= 60
                PrepTimeBucket.LONG -> meal.prepTime > 60
                PrepTimeBucket.NONE -> true
            }
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toJavaLocalDate()
            val period = ChronoUnit.DAYS.between(meal.lastUsed.toJavaLocalDate(), today)
            val cond3 = (filter.lastUsed == 0) || (period <= filter.lastUsed)
            cond1 && cond2 && cond3
        }
        return MealList.fromMeals(filtered)
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

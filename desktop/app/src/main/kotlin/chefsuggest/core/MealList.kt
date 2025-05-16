package chefsuggest.core

import kotlinx.serialization.json.Json
import java.io.File

class MealList private constructor() {
    private val meals = mutableListOf<Meal>()
    private val mealNames = mutableListOf<String>()
//    private val mealTags = mutableListOf<List<String>>()
//    private val mealUrls = mutableListOf<String>()
//    private val mealIngredients = mutableListOf<List<String>>()
//    private val mealPrepTimes = mutableListOf<Int>()
//    private val mealSteps = mutableListOf<List<String>>()
//    private val mealLastUsedTimes = mutableListOf<String>()

    companion object {
        /**
         * @param path The path of the directory containing `Meal` JSON files. Will only load files with ".json"
         * extension.
         */
        fun fromMealDir(path: String): MealList {
            val mealList = MealList()
            val directory = File(path)
            val files = directory.listFiles()?.sorted()?.filter { it.isFile }
            files?.forEach { file ->
                if (file.extension == "json") {
                    val content = file.inputStream().readBytes().toString(Charsets.UTF_8)
                    val meal = Json.decodeFromString<Meal>(content)
                    mealList.addMeal(meal)
                }
            }
            return mealList
        }

        fun fromMeals(meals: List<Meal>): MealList {
            val mealList = MealList()
            for (meal in meals) {
                mealList.addMeal(meal)
            }
            return mealList
        }
    }

    fun getMeals() : List<Meal> {
        return this.meals.toList()
    }

    fun getLength() : Int {
        return this.meals.size
    }

    /**
     * @throws IllegalArgumentException if `Meal`'s name is already in `MealList`
     */
    fun addMeal(meal: Meal) {
        require(meal.name !in mealNames) { "Meal with that name already exists." }
        this.meals.add(meal)
        this.meals.sortBy { it.name }
//        this.mealNames.add(meal.name)
//        this.mealTags.add(meal.tags)
//        this.mealUrls.add(meal.url)
//        this.mealIngredients.add(meal.ingredients)
//        this.mealPrepTimes.add(meal.prepTime)
//        this.mealSteps.add(meal.steps)
//        this.mealLastUsedTimes.add(meal.lastUsed)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is MealList) throw IllegalArgumentException("Expected MealList type")
        for ((i, otherMeal) in other.meals.withIndex()) {
            if (this.meals[i] != otherMeal) return false
        }

        return true
    }

    override fun hashCode(): Int {
        return meals.hashCode()
    }

//    override fun toString(): String {
////        val stringBuilder = StringBuilder()
////        stringBuilder.append("\nMeal\n")
////        stringBuilder.append("\tNames: ${this.mealNames}\n")
////        stringBuilder.append("\tTags: ${this.mealTags}\n")
////        stringBuilder.append("\tUrls: ${this.mealUrls}\n")
////        stringBuilder.append("\tIngredients: ${this.mealIngredients}\n")
////        stringBuilder.append("\tPrep Times: ${this.mealPrepTimes}\n")
////        stringBuilder.append("\tSteps: ${this.mealSteps}\n")
////        stringBuilder.append("\tLast Used: ${this.mealLastUsedTimes}\n")
////        return stringBuilder.toString()
//    }
}

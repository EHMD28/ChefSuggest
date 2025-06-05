package chefsuggest

import chefsuggest.core.Meal
import chefsuggest.core.MealList
import kotlinx.datetime.LocalDate
import java.io.File
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    companion object TestMeals {
        val mealOne = Meal(
            name = "Macaroni and Cheese",
            tags = listOf("pasta", "dairy"),
            prepTime = 60,
            lastUsed = LocalDate(2025, 2, 1)
        )
        val mealTwo = Meal(
            name = "Chocolate Cake",
            tags = listOf("desert", "chocolate"),
            prepTime = 30,
            lastUsed = LocalDate(2025, 2, 15)
        )
        val mealThree = Meal(
            name = "Chicken Noodle Soup",
            tags = listOf("soup", "quick"),
            prepTime = 45,
            lastUsed = LocalDate(2025, 2, 28)
        )
        val mealFour = Meal(
            name = "Spaghetti",
            tags = listOf("pasta", "easy"),
            prepTime = 60,
            lastUsed = LocalDate(2025, 3, 5)
        )
    }

    @Test
    fun loadingMeals() {
        val path = "src/test/kotlin/chefsuggest/resources/LoadTest/TestMeals.tsv"
        val loadedMeals = MealList.fromTsv(Path(path))
        val testMeals = MealList.fromMeals(listOf(mealOne, mealTwo, mealThree, mealFour))
        assertEquals(loadedMeals, testMeals)
    }

    @Test
    fun writeMeals() {
        val path = "src/test/kotlin/chefsuggest/resources/WriteTest/TestMeals.tsv"
        val testMeals = MealList.fromMeals(listOf(mealOne, mealTwo, mealThree, mealFour))
        testMeals.writeToTsv(Path(path))
        val loadedMeals = MealList.fromTsv(Path(path))
        assertEquals(testMeals, loadedMeals)
    }
}

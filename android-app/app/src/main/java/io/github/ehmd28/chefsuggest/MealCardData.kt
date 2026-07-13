package io.github.ehmd28.chefsuggest

data class MealCardData(
    val name: String,
    val isLocked: Boolean = false
)

///**
// * Fetches the list of meals from a statically hosted Google Sheets file.
// */
//fun fetchMeals(): List<Meal> {
//    return listOf(
//        Meal("Macaroni and Cheese", "https://www.southernliving.com/recipes/best-ever-macaroni-and-cheese-recipe"),
//        Meal("Chocolate Cake"),
//        Meal("BBQ Chicken", "https://www.simplyrecipes.com/recipes/barbecued_chicken_on_the_grill/")
//    )
//}

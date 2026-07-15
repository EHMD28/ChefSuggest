package io.github.ehmd28.chefsuggest.ui.screens

import kotlinx.serialization.Serializable

sealed class AppRoutes {
    @Serializable
    object Main
    @Serializable
    object ViewAllSavedMealLists
    @Serializable
    data class ViewSingleSavedMealList(val fileName: String)
}

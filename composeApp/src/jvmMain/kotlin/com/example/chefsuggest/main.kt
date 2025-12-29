package com.example.chefsuggest

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.ehmd28.chefsuggest.chefsuggest.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ChefSuggest",
    ) {
        App()
    }
}
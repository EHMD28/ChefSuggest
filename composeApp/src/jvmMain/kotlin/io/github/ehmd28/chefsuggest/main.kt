package io.github.ehmd28.chefsuggest

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ChefSuggest",
    ) {
        App()
    }
}
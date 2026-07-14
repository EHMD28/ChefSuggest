package io.github.ehmd28.chefsuggest

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object GeneratorConstants {
    const val DEFAULT_NUM_MEALS = 5
    const val MIN_NUM_MEALS = 1
    const val MAX_NUM_MEALS = 20
    const val MEALS_LIST_TSV_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQ5Njea8v0uttwP3xvlWPF7iK_bO8DX7jxBzMu_A37WBYZWlASY32W99rbVCsSeZyl-_b1-ugR8iqmc/pub?gid=0&single=true&output=tsv"
}

object Spacing {
    val none: Dp = 0.dp
    val spaceXXSmall: Dp = 2.dp
    val spaceExtraSmall: Dp = 4.dp
    val spaceSmall: Dp = 8.dp
    val spaceMedium: Dp = 16.dp
    val spaceLarge: Dp = 32.dp
    val spaceExtraLarge: Dp = 64.dp
    val spaceXXLarge: Dp = 128.dp
    val spaceXXXLarge: Dp = 256.dp
}

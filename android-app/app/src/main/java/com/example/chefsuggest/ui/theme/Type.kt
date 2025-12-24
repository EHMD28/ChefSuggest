package com.example.chefsuggest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import com.example.chefsuggest.R

val defaultFontFamily = FontFamily(
    Font(
        resId = R.font.montserrat_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.montserrat_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.montserrat_bold_italic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    )
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = defaultFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = defaultFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = defaultFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = defaultFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = defaultFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = defaultFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = defaultFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = defaultFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = defaultFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = defaultFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = defaultFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = defaultFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = defaultFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = defaultFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = defaultFontFamily),
)


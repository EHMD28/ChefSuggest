package io.github.ehmd28.chefsuggest.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import io.github.ehmd28.chefsuggest.R
import io.github.ehmd28.chefsuggest.SpacingConstants
import java.io.File

@Composable
fun ViewSingleSavedMealsList(
    fileName: String,
    context: Context,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mealNames = getListOfMealsFromFile(fileName, context)
    Column (modifier = modifier.padding(horizontal = SpacingConstants.spaceMedium)) {
        IconButton(onClick = onNavBack) {
            Icon(
                painter = painterResource(R.drawable.back_arrow_icon),
                contentDescription = "Navigate back button."
            )
        }
        mealNames.forEachIndexed { index, name ->
            Text(
                text = "${index + 1}. $name",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun getListOfMealsFromFile(fileName: String, context: Context): List<String> {
    val file = File(context.filesDir, fileName)
    return file.readLines()
}

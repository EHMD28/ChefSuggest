package io.github.ehmd28.chefsuggest.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import io.github.ehmd28.chefsuggest.SpacingConstants
import io.github.ehmd28.chefsuggest.getDisplayNameFromFileName
import java.io.File

@Composable
fun ViewSingleSavedMealsList(
    fileName: String,
    context: Context,
    modifier: Modifier = Modifier
) {
    val mealNames = getListOfMealsFromFile(fileName, context)
    Column (
        modifier = modifier
            .padding(horizontal = SpacingConstants.spaceMedium)
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(SpacingConstants.spaceMedium))
        Text(
            text = getDisplayNameFromFileName(fileName),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(SpacingConstants.spaceMedium))
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

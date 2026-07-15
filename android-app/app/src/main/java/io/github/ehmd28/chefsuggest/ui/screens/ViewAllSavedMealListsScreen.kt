package io.github.ehmd28.chefsuggest.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import io.github.ehmd28.chefsuggest.DateTimeConstants
import io.github.ehmd28.chefsuggest.R
import io.github.ehmd28.chefsuggest.SpacingConstants
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import java.io.File

@Composable
fun ViewAllSavedMealListsScreen(
    context: Context,
    onViewSingleList: (String) -> Unit,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = SpacingConstants.spaceMedium)) {
        IconButton(onClick = onNavBack) {
            Icon(
                painter = painterResource(R.drawable.back_arrow_icon),
                contentDescription = "Back button"
            )
        }
        Spacer(Modifier.height(SpacingConstants.spaceMedium))
        SavedMealsList(context, onViewSingleList = onViewSingleList, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SavedMealsList(context: Context, onViewSingleList: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(verticalArrangement = spacedBy(SpacingConstants.spaceMedium), modifier = modifier) {
        items(context.fileList()) {
            SavedListCard(fileName = it, context = context, onViewSingleList = onViewSingleList)
        }
    }
}

@Composable
fun SavedListCard(fileName: String, context: Context, onViewSingleList: (String) -> Unit, modifier: Modifier = Modifier) {
    val displayDateTime = try {
        val fileDateTime = LocalDateTime.parse(fileName)
        fileDateTime.format(DateTimeConstants.displayFormat)
    } catch (_: IllegalArgumentException) {
        "Invalid Date Format"
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(SpacingConstants.spaceSmall).fillMaxWidth()) {
            Text(
                text = displayDateTime,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
               IconButton(onClick = { onViewSingleList(fileName) }) {
                   Icon(
                       painter = painterResource(R.drawable.show_icon),
                       contentDescription = "View list from $fileName"
                   )
               }
               IconButton(onClick = { deleteFile(fileName, context) }) {
                   Icon(
                       painter = painterResource(R.drawable.trash_icon),
                       contentDescription = "View list from $fileName"
                   )
               }
            }
        }
    }
}

private fun deleteFile(fileName: String, context: Context) {
    val file = File(context.filesDir, fileName)
    file.delete()
}

package io.github.ehmd28.chefsuggest.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.github.ehmd28.chefsuggest.ChefSuggestViewModel
import io.github.ehmd28.chefsuggest.GeneratorConstants
import io.github.ehmd28.chefsuggest.MealCardData
import io.github.ehmd28.chefsuggest.R
import io.github.ehmd28.chefsuggest.SpacingConstants
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun HomeScreen(
    context: Context,
    modifier: Modifier = Modifier,
    appViewModel: ChefSuggestViewModel,
    onViewSavedMealLists: () -> Unit,
    onSaveMealList: () -> Unit,
) {
    val uiState by appViewModel.uiState.collectAsState()
    Column(modifier = modifier.padding(horizontal = SpacingConstants.spaceMedium)) {
        Spacer(modifier = Modifier.height(SpacingConstants.spaceMedium))
        NumMealsSelector(
            updateNumMealsFn = { n -> appViewModel.updateNumMeals(n) }
        )
        Spacer(modifier = Modifier.height(SpacingConstants.spaceMedium))
        GenerateMealsButton(
            onClick = { appViewModel.generateMeals() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        ActionIconButtons(
            onSave = {
                handleSave(
                    content = appViewModel.generateSelectedMealsFile(),
                    context = context,
                )
                onSaveMealList()
            },
            onShow = onViewSavedMealLists,
            onShare = {
                handleShare(
                    content = appViewModel.generatePrettySelectedMealsFile(),
                    context = context
                )
            },
            onRefresh = { appViewModel.updateAllMealsFromRemote() },
        )
        Spacer(modifier = Modifier.height(SpacingConstants.spaceMedium))
        MealsList(uiState.selectedMeals, toggleLockAtIndex = { appViewModel.toggleLockAtIndex(it) })
    }
}

@OptIn(ExperimentalTime::class)
private fun handleSave(content: String, context: Context) {
    val currentInstant = Clock.System.now()
    val today = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    val fileName = today.format(LocalDateTime.Formats.ISO)
    context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
        it.write(content.toByteArray())
    }
}

private fun handleShare(content: String, context: Context) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val title = context.resources.getString(R.string.send_chooser_title)
    val chooser = Intent.createChooser(sendIntent, title)
    val pm = context.packageManager
    if (sendIntent.resolveActivity(pm) != null) {
        context.startActivity(chooser)
    }
}

@Composable
fun NumMealsSelector(updateNumMealsFn: (Int) -> Unit, modifier: Modifier = Modifier) {
    var numMeals by remember { mutableIntStateOf(GeneratorConstants.DEFAULT_NUM_MEALS) }

    fun internalUpdateNumMeals(n: Int) {
        numMeals = n
        updateNumMealsFn(numMeals)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
    ) {
        NumMealsSelectorDecrementButton(
            numMeals,
            ::internalUpdateNumMeals,
            modifier = Modifier.weight(1f)
        )
        NumMealsSelectorTextField(
            numMeals,
            ::internalUpdateNumMeals,
            modifier = Modifier.weight(3f)
        )
        NumMealsSelectorIncrementButton(
            numMeals,
            ::internalUpdateNumMeals,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NumMealsSelectorTextField(
    numMeals: Int,
    updateNumMealsFn: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        label = { Text("Number of Meals to Generate") },
        value = numMeals.toString(),
        onValueChange = { value ->
            val valueAsInt =
                if (value.isEmpty()) GeneratorConstants.MIN_NUM_MEALS else value.toInt()
            updateNumMealsFn(valueAsInt)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            try {
                val constrainedValue = max(
                    min(numMeals, GeneratorConstants.MAX_NUM_MEALS),
                    GeneratorConstants.MIN_NUM_MEALS
                )
                updateNumMealsFn(constrainedValue)
            } catch (_: NumberFormatException) {
            }
            focusManager.clearFocus()
        }),
        singleLine = true,
        modifier = modifier
    )
}

@Composable
private fun NumMealsSelectorDecrementButton(
    numMeals: Int,
    updatedNumMealsFn: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = {
        val n = max(numMeals - 1, GeneratorConstants.MIN_NUM_MEALS)
        updatedNumMealsFn(n)
    }, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.remove_icon),
            contentDescription = stringResource(R.string.num_meals_decrement_button),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun NumMealsSelectorIncrementButton(
    numMeals: Int,
    updatedNumMealsFn: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = {
        val n = min(numMeals + 1, GeneratorConstants.MAX_NUM_MEALS)
        updatedNumMealsFn(n)
    }, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.add_icon),
            contentDescription = stringResource(R.string.increment_num_meals_button),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun GenerateMealsButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Text(
            text = "Generate Meals",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ActionIconButtons(
    onSave: () -> Unit,
    onShow: () -> Unit,
    onShare: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = modifier.fillMaxWidth()) {
        IconButton(onClick = onSave) {
            Icon(
                painter = painterResource(R.drawable.save_icon),
                contentDescription = stringResource(R.string.save_meals_icon),
            )
        }
        IconButton(onClick = onShow) {
            Icon(
                painter = painterResource(R.drawable.show_icon),
                contentDescription = stringResource(R.string.show_meals_icon),
            )
        }
        IconButton(onClick = onShare) {
            Icon(
                painter = painterResource(R.drawable.share_icon),
                contentDescription = stringResource(R.string.share_meals_icon),
            )
        }
        IconButton(onClick = onRefresh) {
            Icon(
                painter = painterResource(R.drawable.refresh_icon),
                contentDescription = stringResource(R.string.refresh_meals_icon),
            )
        }
    }
}

@Composable
fun MealsList(
    meals: List<MealCardData?>,
    modifier: Modifier = Modifier,
    toggleLockAtIndex: (Int) -> Unit
) {
    LazyColumn(verticalArrangement = spacedBy(SpacingConstants.spaceMedium), modifier = modifier) {
        itemsIndexed(meals) { index, meal ->
            MealCard(index, meal = meal, toggleLockAtIndex = toggleLockAtIndex)
        }
    }
}

@Composable
fun MealCard(
    index: Int,
    toggleLockAtIndex: (Int) -> Unit,
    modifier: Modifier = Modifier,
    meal: MealCardData? = null
) {
    val mealName = meal?.name ?: "Meal Name"
    val isMealLocked = meal?.isLocked ?: false
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(SpacingConstants.spaceSmall)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
            ) {
                Text(
                    "${index + 1}. $mealName",
                    color = if (isMealLocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(onClick = { toggleLockAtIndex(index) }) {
                Icon(
                    painter = painterResource(if (isMealLocked) R.drawable.lock_closed_icon else R.drawable.unlocked_icon),
                    contentDescription = stringResource(R.string.locked_icon)
                )
            }
        }
    }
}

@Composable
fun SavedMealListPopup() {
    val cardBackground = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
    Card(
        colors = CardDefaults.cardColors(containerColor = cardBackground),
    ) {
        Text(
            text = "Meal List Saved",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(SpacingConstants.spaceMedium)
        )
    }
}

@Preview
@Composable
fun NumMealsSelectorPreview() {
    NumMealsSelector(
        updateNumMealsFn = { }
    )
}

@Preview
@Composable
fun MealCardPreview() {
    MealCard(
        index = 0,
        toggleLockAtIndex = { }
    )
}

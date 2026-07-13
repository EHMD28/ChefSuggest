package io.github.ehmd28.chefsuggest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.ehmd28.chefsuggest.ui.theme.ChefSuggestTheme
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChefSuggestTheme(darkTheme = false, dynamicColor = false) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Chef Suggest") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    appViewModel: ChefSuggestViewModel = viewModel(),
) {
    val uiState by appViewModel.uiState.collectAsState()
    Column(modifier = modifier.padding(horizontal = Spacing.spaceMedium)) {
        Spacer(modifier = Modifier.height(Spacing.spaceMedium))
        NumMealsSelector(
            updateNumMealsFun = { n -> appViewModel.updateNumMeals(n) }
        )
        Spacer(modifier = Modifier.height(Spacing.spaceMedium))
        GenerateMealsButton(
            onClick = { appViewModel.generateMeals() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        ActionIcons()
        Spacer(modifier = Modifier.height(Spacing.spaceMedium))
        MealsList(uiState.selectedMeals, toggleLockAtIndex = { appViewModel.toggleLockAtIndex(it) })
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
fun ActionIcons(modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = modifier.fillMaxWidth()) {
        IconButton(onClick = {  }) {
            Icon(
                painter = painterResource(R.drawable.save_icon),
                contentDescription = "Save meals icon.",
            )
        }
        IconButton(onClick = {  }) {
            Icon(
                painter = painterResource(R.drawable.show_icon),
                contentDescription = "Show meals icon.",
            )
        }
        IconButton(onClick = {  }) {
            Icon(
                painter = painterResource(R.drawable.share_icon),
                contentDescription = "Share meals icon.",
            )
        }
        IconButton(onClick = {  }) {
            Icon(
                painter = painterResource(R.drawable.refresh_icon),
                contentDescription = "Refresh meals icon.",
            )
        }
    }
}

@Composable
fun NumMealsSelector(updateNumMealsFun: (Int) -> Unit, modifier: Modifier = Modifier) {
    var numMeals by remember { mutableIntStateOf(GeneratorConstants.DEFAULT_NUM_MEALS) }

    fun internalUpdateNumMeals(n: Int) {
        numMeals = n
        updateNumMealsFun(numMeals)
    }

    Row (verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
    ) {

        NumMealsSelectorDecrementButton(numMeals, ::internalUpdateNumMeals, modifier = Modifier.weight(1f))
        NumMealsSelectorTextField(numMeals, ::internalUpdateNumMeals, modifier = Modifier.weight(3f))
        NumMealsSelectorIncrementButton(numMeals, ::internalUpdateNumMeals, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun NumMealsSelectorTextField(numMeals: Int, updateNumMealsFn: (Int) -> Unit, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        label = { Text("Number of Meals to Generate") },
        value = numMeals.toString(),
        onValueChange = { value ->
            try {
                var valueAsInt = if (value.isEmpty()) GeneratorConstants.MIN_NUM_MEALS else value.toInt()
                valueAsInt = max(min(valueAsInt, GeneratorConstants.MAX_NUM_MEALS), GeneratorConstants.MIN_NUM_MEALS)
                updateNumMealsFn(valueAsInt)
            }
            catch (_: NumberFormatException) { }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        singleLine = true,
        modifier = modifier
    )
}

@Composable
private fun NumMealsSelectorDecrementButton(numMeals: Int, updatedNumMealsFn: (Int) -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = {
        val n = max(numMeals - 1, GeneratorConstants.MIN_NUM_MEALS)
        updatedNumMealsFn(n)
    }, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.remove_icon),
            contentDescription = "Decrease the number of meals.",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun NumMealsSelectorIncrementButton(numMeals: Int, updatedNumMealsFn: (Int) -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = {
        val n = min(numMeals + 1, GeneratorConstants.MAX_NUM_MEALS)
        updatedNumMealsFn(n)
    }, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.add_icon),
            contentDescription = "Increase the number of meals",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun MealsList(meals: List<MealCardData?>, modifier: Modifier = Modifier, toggleLockAtIndex: (Int) -> Unit) {
    LazyColumn(verticalArrangement = spacedBy(Spacing.spaceMedium), modifier = modifier) {
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
    Card(modifier = modifier.background(color = MaterialTheme.colorScheme.surface)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .padding(Spacing.spaceSmall)
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.weight(3f).horizontalScroll(rememberScrollState())) {
                Text(
                    "${index + 1}. $mealName",
                    color = if (isMealLocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(onClick = { toggleLockAtIndex(index) }) {
                Icon(
                    painter = painterResource(if (isMealLocked) R.drawable.lock_closed_icon else R.drawable.lock_open_icon),
                    contentDescription = "Locked icon."
                )
            }
        }
    }
}

@Preview
@Composable
fun NumMealsSelectorPreview() {
    NumMealsSelector(
        updateNumMealsFun = { x -> Unit }
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

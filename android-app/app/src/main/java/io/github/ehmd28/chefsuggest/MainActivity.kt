package io.github.ehmd28.chefsuggest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.ehmd28.chefsuggest.ui.layout.Spacing
import io.github.ehmd28.chefsuggest.ui.theme.ChefSuggestTheme

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
            updateNumMealsCallback = { }
        )
        Spacer(modifier = Modifier.height(Spacing.spaceMedium))
        GenerateMealsButton(modifier = Modifier.align(Alignment.CenterHorizontally))
        ActionIcons()
        Spacer(modifier = Modifier.height(Spacing.spaceMedium))
        MealsList(uiState.mealsList)
    }
}

//@Composable
//private fun AppTitle() {
//    Text(
//        text = "Chef Suggest",
//        color = MaterialTheme.colorScheme.primary,
//        style = MaterialTheme.typography.titleLarge,
//        textAlign = TextAlign.Center,
//        modifier = Modifier.fillMaxWidth()
//    )
//}


@Composable
fun GenerateMealsButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { },
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
fun NumMealsSelector(updateNumMealsCallback: (Int) -> Unit, modifier: Modifier = Modifier) {
    var numMeals by remember { mutableIntStateOf(5) }
    Row (verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = { numMeals-- }, modifier = Modifier.weight(1f)) {
            Icon(
                painter = painterResource(R.drawable.remove_icon),
                contentDescription = "Decrease the number of meals.",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        OutlinedTextField(
            label = { Text("Number of Meals to Generate") },
            value = numMeals.toString(),
            onValueChange = { value ->
                try {
                    numMeals = value.toInt()
                    updateNumMealsCallback(numMeals)
                } catch (e: NumberFormatException) {
                    updateNumMealsCallback(0)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(3f)
        )
        IconButton(onClick = { numMeals++  }) {
            Icon(
                painter = painterResource(R.drawable.add_icon),
                contentDescription = "Increase the number of meals",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MealsList(meals: List<Meal>, modifier: Modifier = Modifier) {
    LazyColumn(verticalArrangement = spacedBy(Spacing.spaceMedium), modifier = modifier) {
        itemsIndexed(meals) { index, meal ->
            MealCard(index, meal)
        }
    }
}

@Composable
fun MealCard(index: Int, meal: Meal, modifier: Modifier = Modifier) {
    Card(modifier = modifier.background(color = MaterialTheme.colorScheme.surface)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .padding(Spacing.spaceSmall)
                .fillMaxWidth()
        ) {
            Text(
                "${index + 1}. ${meal.name}",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(3f)
            )
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(1f)) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(if (meal.isLocked) R.drawable.lock_closed_icon else R.drawable.lock_open_icon),
                        contentDescription = "Locked icon."
                    )
                }
                if (meal.recipeLink != null) {
                    Spacer(modifier = Modifier.width(Spacing.spaceSmall))
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.link_icon),
                            contentDescription = "Recipe link icon."
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NumMealsSelectorPreview() {
    NumMealsSelector(
        updateNumMealsCallback = { x -> Unit }
    )
}

@Preview
@Composable
fun MealCardPreview() {
    MealCard(
        index = 0,
        meal = Meal("Macaroni and Cheese", "https://www.southernliving.com/recipes/best-ever-macaroni-and-cheese-recipe")
    )
}

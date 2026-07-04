package io.github.ehmd28.chefsuggest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.ehmd28.chefsuggest.ui.theme.ChefSuggestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChefSuggestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val meals = fetchMeals()
    Column(modifier) {
        Text("Meals", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
        MealsList(meals, modifier)
    }
}

@Composable
fun MealsList(meals: List<Meal>, modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        items(meals) { meal ->
            MealCard(meal)
        }
    }
}

@Composable
fun MealCard(meal: Meal, modifier: Modifier = Modifier) {
    Card(modifier = modifier.background(color = MaterialTheme.colorScheme.surface)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(
                meal.name,
                color = MaterialTheme.colorScheme.primary,
            )
            Icon(
                painter = painterResource(if (meal.isLocked) R.drawable.lock_closed_icon else R.drawable.lock_open_icon),
                contentDescription = "Locked icon."
            )
            if (meal.recipeLink != null) {
                Icon(
                    painter = painterResource(R.drawable.link_icon),
                    contentDescription = "Recipe link icon."
                )
            }
        }
    }
}

@Preview
@Composable
fun MealCardPreview() {
    val meals = fetchMeals();
    MealCard(meals[0])
}
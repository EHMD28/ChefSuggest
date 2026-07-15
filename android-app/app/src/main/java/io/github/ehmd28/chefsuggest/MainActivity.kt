package io.github.ehmd28.chefsuggest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.ehmd28.chefsuggest.ui.screens.AppRoutes
import io.github.ehmd28.chefsuggest.ui.theme.ChefSuggestTheme
import io.github.ehmd28.chefsuggest.ui.screens.HomeScreen
import io.github.ehmd28.chefsuggest.ui.screens.ViewAllSavedMealListsScreen
import io.github.ehmd28.chefsuggest.ui.screens.ViewSingleSavedMealsList

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChefSuggestTheme(darkTheme = false, dynamicColor = false) {
                Scaffold(
                    topBar = {
                        AppTopBar()
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val appViewModel: ChefSuggestViewModel = viewModel()
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.Main,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<AppRoutes.Main> {
                            HomeScreen(
                                context = context,
                                appViewModel = appViewModel,
                                onViewSavedMealLists = { navController.navigate(AppRoutes.ViewAllSavedMealLists) }
                            )
                        }
                        composable<AppRoutes.ViewAllSavedMealLists> {
                            ViewAllSavedMealListsScreen(
                                onViewSingleList = { fileName -> navController.navigate(AppRoutes.ViewSingleSavedMealList(fileName)) },
                                onNavBack = { navController.navigate(AppRoutes.Main) },
                                context = context
                            )
                        }
                        composable<AppRoutes.ViewSingleSavedMealList> { backStackEntry ->
                            val singleMealListDetails: AppRoutes.ViewSingleSavedMealList = backStackEntry.toRoute()
                            ViewSingleSavedMealsList(
                                fileName = singleMealListDetails.fileName,
                                context = context,
                                onNavBack = { navController.navigate(AppRoutes.ViewAllSavedMealLists) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    CenterAlignedTopAppBar(
        title = { Text("Chef Suggest") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

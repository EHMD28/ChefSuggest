package io.github.ehmd28.chefsuggest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.ehmd28.chefsuggest.ui.screens.AppRoutes
import io.github.ehmd28.chefsuggest.ui.theme.ChefSuggestTheme
import io.github.ehmd28.chefsuggest.ui.screens.HomeScreen
import io.github.ehmd28.chefsuggest.ui.screens.SavedMealListPopup
import io.github.ehmd28.chefsuggest.ui.screens.ViewAllSavedMealListsScreen
import io.github.ehmd28.chefsuggest.ui.screens.ViewSingleSavedMealsList

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChefSuggestTheme(darkTheme = false, dynamicColor = false) {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow_icon),
                        contentDescription = stringResource(R.string.back_arrow_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@Composable
fun App(
    appViewModel: ChefSuggestViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // For whatever reason, the TopAppBar will not recompose unless `backStackEntry` is referenced.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = backStackEntry?.let { navController.previousBackStackEntry != null } ?: false

    Scaffold(
        topBar = {
            AppTopBar(
                canNavigateBack = canNavigateBack,
                navigateBack = { navController.navigateUp() }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
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
                    onViewSavedMealLists = { navController.navigate(AppRoutes.ViewAllSavedMealLists) },
                    onSaveMealList = { navController.navigate(AppRoutes.SavedListPopup) }
                )
            }
            composable<AppRoutes.ViewAllSavedMealLists> {
                ViewAllSavedMealListsScreen(
                    onViewSingleList = { fileName -> navController.navigate(AppRoutes.ViewSingleSavedMealList(fileName)) },
                    context = context
                )
            }
            dialog<AppRoutes.SavedListPopup> {
                SavedMealListPopup()
            }
            composable<AppRoutes.ViewSingleSavedMealList> { backStackEntry ->
                val singleMealListDetails: AppRoutes.ViewSingleSavedMealList = backStackEntry.toRoute()
                ViewSingleSavedMealsList(
                    fileName = singleMealListDetails.fileName,
                    context = context,
                )
            }
        }
    }
}

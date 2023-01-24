package com.example.nomenclature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nomenclature.presentation.Constants
import com.example.nomenclature.presentation.Screen
import com.example.nomenclature.presentation.main.MainScreen
import com.example.nomenclature.presentation.scanner.ScannerScreen
import com.example.nomenclature.presentation.search.SearchScreen
import com.example.nomenclature.presentation.search_product.SearchProductScreen
import com.example.nomenclature.presentation.storage_utils_item.StorageItemScreen
import com.example.nomenclature.ui.theme.NomenclatureTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NomenclatureTheme {
                TransparentSystemBars()
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(navController, Modifier)
                }
            }
        }
    }
}

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent)
        systemUiController.setNavigationBarColor(
            darkIcons = !isDarkTheme,
            color = Color.Transparent,
            navigationBarContrastEnforced = false,
        )
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !isDarkTheme
        )
    }
}

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier) {
    NavHost(
        startDestination = Screen.MainScreen.route,
        navController = navController,
        modifier = modifier,
    ) {
        composable(
            route = Screen.MainScreen.route,
        ) {
            MainScreen(navController = navController)
        }
        composable(
            route = Screen.ScannerScreen.route,
        ) {
            ScannerScreen(navController = navController)
        }
        composable(
            route = Screen.SearchScreen.route,
        ) {
            SearchScreen(navController = navController)
        }
        composable(
            route = "${Screen.StorageItemScreen.route}/{${Constants.STORAGE_ITEM_ID_PARAM}}",
        ) {
            StorageItemScreen(navController = navController)
        }
        composable(
            route = "${Screen.FromAddingScannerStorageItemScreen.route}/{${Constants.STORAGE_ITEM_ID_PARAM}}",
        ) {
            StorageItemScreen(navController = navController, isSearchMode = false)
        }
        composable(
            route = Screen.QRScannerScreen.route,
        ) {
            ScannerScreen(navController = navController, isSearchMode = true)
        }
        composable(
            route = Screen.ProductSearchScreen.route,
        ) {
            SearchProductScreen(navController = navController)
        }
    }
}
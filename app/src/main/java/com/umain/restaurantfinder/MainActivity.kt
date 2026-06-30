package com.umain.restaurantfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.umain.restaurantfinder.ui.detail.RestaurantDetailScreen
import com.umain.restaurantfinder.ui.detail.RestaurantDetailViewModel
import com.umain.restaurantfinder.ui.list.RestaurantListScreen
import com.umain.restaurantfinder.ui.theme.RestaurantfinderandroidTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantfinderandroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {
                        composable("list") {
                            RestaurantListScreen(
                                paddingValues = paddingValues,
                                onRestaurantClick = { restaurant ->
                                    navController.navigate("detail/${restaurant.id}")
                                }
                            )
                        }
                        composable("detail/{restaurantId}") { backStackEntry ->
                            val restaurantId = backStackEntry.arguments
                                ?.getString("restaurantId") ?: return@composable

                            val viewModel: RestaurantDetailViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsState()

                            LaunchedEffect(restaurantId) {
                                viewModel.loadRestaurant(restaurantId)
                            }

                            RestaurantDetailScreen(
                                uiState = uiState,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

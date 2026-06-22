package com.example.restaurant_finder_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurant_finder_android.data.Filter
import com.example.restaurant_finder_android.data.Restaurant
import com.example.restaurant_finder_android.ui.detail.RestaurantDetailScreen
import com.example.restaurant_finder_android.ui.list.RestaurantListScreen
import com.example.restaurant_finder_android.ui.theme.RestaurantfinderandroidTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
                                onRestaurantClick = { restaurant, filters ->
                                    val restaurantJson = java.net.URLEncoder.encode(
                                        Json.encodeToString(restaurant), "UTF-8"
                                    )
                                    val filtersJson = java.net.URLEncoder.encode(
                                        Json.encodeToString(filters), "UTF-8"
                                    )
                                    navController.navigate("detail/$restaurantJson/$filtersJson")
                                }
                            )
                        }
                        composable("detail/{restaurantJson}/{filtersJson}") { backStackEntry ->
                            val restaurantEncoded = backStackEntry.arguments
                                ?.getString("restaurantJson") ?: return@composable
                            val filtersEncoded = backStackEntry.arguments
                                ?.getString("filtersJson") ?: return@composable

                            val restaurant = remember(restaurantEncoded) {
                                Json.decodeFromString<Restaurant>(
                                    java.net.URLDecoder.decode(restaurantEncoded, "UTF-8")
                                )
                            }
                            val filters = remember(filtersEncoded) {
                                Json.decodeFromString<List<Filter>>(
                                    java.net.URLDecoder.decode(filtersEncoded, "UTF-8")
                                )
                            }
                            RestaurantDetailScreen(
                                restaurant = restaurant,
                                filters = filters,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
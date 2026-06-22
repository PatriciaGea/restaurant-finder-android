package com.example.restaurant_finder_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                        startDestination = "list",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("list") {
                            RestaurantListScreen(
                                onRestaurantClick = { restaurant ->
                                    val json = Json.encodeToString(restaurant)
                                    val encoded = java.net.URLEncoder.encode(json, "UTF-8")
                                    navController.navigate("detail/$encoded")
                                }
                            )
                        }
                        composable("detail/{restaurantJson}") { backStackEntry ->
                            val encoded = backStackEntry.arguments?.getString("restaurantJson") ?: return@composable
                            val json = java.net.URLDecoder.decode(encoded, "UTF-8")
                            val restaurant = remember(json) {
                                Json.decodeFromString<Restaurant>(json)
                            }
                            RestaurantDetailScreen(
                                restaurant = restaurant,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
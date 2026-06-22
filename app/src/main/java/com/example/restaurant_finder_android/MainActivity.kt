package com.example.restaurant_finder_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.restaurant_finder_android.ui.list.RestaurantListScreen
import com.example.restaurant_finder_android.ui.theme.RestaurantfinderandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantfinderandroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RestaurantListScreen(
                        onRestaurantClick = { restaurant ->
                            // next screen
                        }
                    )
                }
            }
        }
    }
}
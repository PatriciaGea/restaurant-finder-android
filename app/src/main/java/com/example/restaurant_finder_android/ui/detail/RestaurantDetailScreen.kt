package com.example.restaurant_finder_android.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.restaurant_finder_android.data.Filter
import com.example.restaurant_finder_android.data.Restaurant
import com.example.restaurant_finder_android.ui.list.DarkText
import com.example.restaurant_finder_android.ui.list.NegativeRed
import com.example.restaurant_finder_android.ui.list.PositiveGreen
import com.example.restaurant_finder_android.ui.list.SubtitleGray

@Composable
fun RestaurantDetailScreen(
    restaurant: Restaurant,
    filters: List<Filter> = emptyList(),
    viewModel: RestaurantDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(restaurant.id) {
        viewModel.loadOpenStatus(restaurant.id)
    }

    val tagNames = restaurant.filterIds
        .mapNotNull { id -> filters.find { it.id == id }?.name }
        .joinToString(" · ")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        AsyncImage(
            model = restaurant.imageUrl,
            contentDescription = restaurant.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 40.dp)
                .size(36.dp)
                .align(Alignment.TopStart)
                .clickable { onBack() }
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Back",
                tint = Color(0xFF1F2B2E),
                modifier = Modifier
                    .size(63.dp)
                    .align(Alignment.Center)
            )
        }

        Card(
            modifier = Modifier
                .padding(start = 16.dp, top = 175.dp)
                .width(343.dp)
                .defaultMinSize(minHeight = 144.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        )  {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = restaurant.name,
                    color = DarkText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                if (tagNames.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = tagNames,
                        color = SubtitleGray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                when {
                    uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    uiState.errorMessage != null -> Text(
                        text = uiState.errorMessage ?: "",
                        color = NegativeRed,
                        fontSize = 14.sp
                    )
                    else -> {
                        val isOpen = uiState.isOpen == true
                        Text(
                            text = if (isOpen) "Open" else "Closed",
                            color = if (isOpen) PositiveGreen else NegativeRed,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}
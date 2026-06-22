package com.example.restaurant_finder_android.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.restaurant_finder_android.data.Filter
import com.example.restaurant_finder_android.data.Restaurant

// Cores do design (Figma)
val DarkText = Color(0xFF1F2B2E)
val SubtitleGray = Color(0xFF999999)
val BackgroundGray = Color(0xFFF8F8F8)
val SelectedOrange = Color(0xFFE2A364)
val PositiveGreen = Color(0xFF2ECC71)
val NegativeRed = Color(0xFFC0392B)

@Composable
fun RestaurantListScreen(
    viewModel: RestaurantListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onRestaurantClick: (Restaurant) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = NegativeRed,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        FilterRow(
                            filters = uiState.filters,
                            selectedFilterIds = uiState.selectedFilterIds,
                            onFilterClick = viewModel::onFilterClicked
                        )
                    }
                    items(uiState.visibleRestaurants, key = { it.id }) { restaurant ->
                        RestaurantCard(
                            restaurant = restaurant,
                            onClick = { onRestaurantClick(restaurant) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    filters: List<Filter>,
    selectedFilterIds: Set<String>,
    onFilterClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters, key = { it.id }) { filter ->
            FilterChip(
                filter = filter,
                isSelected = filter.id in selectedFilterIds,
                onClick = { onFilterClick(filter.id) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    filter: Filter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) SelectedOrange else Color.White
    val textColor = if (isSelected) Color.White else DarkText

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
    ) {
        AsyncImage(
            model = filter.imageUrl,
            contentDescription = filter.name,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = filter.name,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = Color.White
    ) {
        Column {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = restaurant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = restaurant.name,
                        color = DarkText,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = NegativeRed,
                            modifier = Modifier.size(14.dp)
                        )
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "${restaurant.deliveryTimeMinutes} mins",
                            color = SubtitleGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = SelectedOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = restaurant.rating.toString(),
                        color = DarkText,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
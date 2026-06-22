package com.example.restaurant_finder_android.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.restaurant_finder_android.data.Filter
import com.example.restaurant_finder_android.data.Restaurant
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import com.example.restaurant_finder_android.R

val DarkText = Color(0xFF1F2B2E)
val SubtitleGray = Color(0xFF999999)
val BackgroundGray = Color(0xFFF8F8F8)
val SelectedOrange = Color(0xFFE2A364)
val PositiveGreen = Color(0xFF2ECC71)
val NegativeRed = Color(0xFFC0392B)

@Composable
fun RestaurantListScreen(
    viewModel: RestaurantListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onRestaurantClick: (Restaurant, List<Filter>) -> Unit,
    paddingValues: PaddingValues = PaddingValues()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(BackgroundGray)
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
            uiState.errorMessage != null -> Text(
                text = uiState.errorMessage ?: "",
                color = NegativeRed,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            )
            else -> {
                LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                    item {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(98.dp)
                                    .background(Color.White)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.header),
                                    contentDescription = "Munchies logo",
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 44.dp)
                                        .width(55.dp)
                                        .height(54.dp)
                                        .align(Alignment.TopStart),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(9.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFFF8F8F8),
                                                Color(0x00F8F8F8)
                                            )
                                        )
                                    )
                            )
                        }
                    }

                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.filters, key = { it.id }) { filter ->
                                FilterChip(
                                    filter = filter,
                                    isSelected = filter.id in uiState.selectedFilterIds,
                                    onClick = { viewModel.onFilterClicked(filter.id) }
                                )
                            }
                        }
                    }

                    items(uiState.visibleRestaurants, key = { it.id }) { restaurant ->
                        RestaurantCard(
                            restaurant = restaurant,
                            filters = uiState.filters,
                            onClick = { onRestaurantClick(restaurant, uiState.filters) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
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
            .width(144.dp)
            .height(48.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(50),
                ambientColor = Color(0x1A000000),
                spotColor = Color(0x1A000000)
            )
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(start = 6.dp, end = 12.dp)
    ) {
        AsyncImage(
            model = filter.imageUrl,
            contentDescription = filter.name,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = filter.name,
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            maxLines = 1
        )
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    filters: List<Filter> = emptyList(),
    onClick: () -> Unit
) {
    val tagNames = restaurant.filterIds
        .mapNotNull { id -> filters.find { it.id == id }?.name }
        .joinToString(" · ")

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .width(343.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                ambientColor = Color(0x1A000000),
                spotColor = Color(0x1A000000)
            )
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Column {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = restaurant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        color = DarkText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = SelectedOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = restaurant.rating.toString(),
                            color = DarkText,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
                if (tagNames.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tagNames,
                        color = SubtitleGray,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = NegativeRed,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${restaurant.deliveryTimeMinutes} mins",
                        color = SubtitleGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
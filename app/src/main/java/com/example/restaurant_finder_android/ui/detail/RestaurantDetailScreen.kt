package com.example.restaurant_finder_android.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.restaurant_finder_android.data.Restaurant
import com.example.restaurant_finder_android.ui.list.BackgroundGray
import com.example.restaurant_finder_android.ui.list.DarkText
import com.example.restaurant_finder_android.ui.list.NegativeRed
import com.example.restaurant_finder_android.ui.list.PositiveGreen
import com.example.restaurant_finder_android.ui.list.SubtitleGray

@Composable
fun RestaurantDetailScreen(
    restaurant: Restaurant,
    viewModel: RestaurantDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(restaurant.id) {
        viewModel.loadOpenStatus(restaurant.id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // Imagem no topo
        AsyncImage(
            model = restaurant.imageUrl,
            contentDescription = restaurant.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f),
            contentScale = ContentScale.Crop
        )

        // Botão de voltar
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = DarkText
            )
        }

        // Card branco flutuando sobre a imagem
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.TopStart)
                .offset(y = 200.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = restaurant.name,
                    color = DarkText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.size(8.dp))

                // Status aberto/fechado
                when {
                    uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    uiState.errorMessage != null -> Text(
                        text = uiState.errorMessage ?: "",
                        color = NegativeRed
                    )
                    else -> {
                        val isOpen = uiState.isOpen == true
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isOpen) PositiveGreen else NegativeRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isOpen) "Open" else "Closed",
                                color = if (isOpen) PositiveGreen else NegativeRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "${restaurant.deliveryTimeMinutes} mins delivery",
                    color = SubtitleGray
                )
            }
        }
    }
}
package com.umain.restaurantfinder.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val id: String,
    val name: String,
    val rating: Double,
    val filterIds: List<String> = emptyList(),
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("delivery_time_minutes")
    val deliveryTimeMinutes: Int
)

@Serializable
data class RestaurantsResponse(
    val restaurants: List<Restaurant>
)

@Serializable
data class Filter(
    val id: String,
    val name: String,
    @SerialName("image_url")
    val imageUrl: String
)

@Serializable
data class OpenStatus(
    @SerialName("restaurant_id")
    val restaurantId: String,
    @SerialName("is_currently_open")
    val isCurrentlyOpen: Boolean
)

@Serializable
data class ApiError(
    val error: Boolean,
    val reason: String
)
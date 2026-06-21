package com.example.restaurant_finder_android.data

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RestaurantRepository(
    private val api: RestaurantApi = NetworkModule.api
) {

    suspend fun getRestaurants(): List<Restaurant> {
        return api.getRestaurants().restaurants
    }

    suspend fun getFiltersFor(restaurants: List<Restaurant>): List<Filter> = coroutineScope {
        val uniqueFilterIds = restaurants
            .flatMap { it.filterIds }
            .distinct()

        uniqueFilterIds
            .map { id -> async { api.getFilter(id) } }
            .awaitAll()
    }

    suspend fun getOpenStatus(restaurantId: String): OpenStatus {
        return api.getOpenStatus(restaurantId)
    }
}
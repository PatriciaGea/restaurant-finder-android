package com.umain.restaurantfinder.data

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class RestaurantRepository(
    private val api: RestaurantApi = NetworkModule.api
) {

    private var cachedRestaurants: List<Restaurant> = emptyList()
    private var cachedFilters: List<Filter> = emptyList()

    suspend fun getRestaurants(): List<Restaurant> {
        val restaurants = api.getRestaurants().restaurants
        cachedRestaurants = restaurants
        return restaurants
    }

    suspend fun getFiltersFor(restaurants: List<Restaurant>): List<Filter> = coroutineScope {
        val uniqueFilterIds = restaurants
            .flatMap { it.filterIds }
            .distinct()

        val filters = uniqueFilterIds
            .map { id -> async { api.getFilter(id) } }
            .awaitAll()
        cachedFilters = filters
        filters
    }

    suspend fun getOpenStatus(restaurantId: String): OpenStatus {
        return api.getOpenStatus(restaurantId)
    }

    fun getCachedRestaurantById(id: String): Restaurant? {
        return cachedRestaurants.find { it.id == id }
    }

    fun getCachedFilters(): List<Filter> {
        return cachedFilters
    }
}
package com.example.restaurant_finder_android.data

import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantApi {

    @GET("restaurants")
    suspend fun getRestaurants(): RestaurantsResponse

    @GET("filter/{id}")
    suspend fun getFilter(@Path("id") id: String): Filter

    @GET("open/{id}")
    suspend fun getOpenStatus(@Path("id") id: String): OpenStatus
}
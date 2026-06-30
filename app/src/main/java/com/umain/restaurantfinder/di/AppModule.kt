package com.umain.restaurantfinder.di

import com.umain.restaurantfinder.data.RestaurantRepository
import com.umain.restaurantfinder.ui.detail.RestaurantDetailViewModel
import com.umain.restaurantfinder.ui.list.RestaurantListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RestaurantRepository() }
    viewModel { RestaurantListViewModel(get()) }
    viewModel { RestaurantDetailViewModel(get()) }
}

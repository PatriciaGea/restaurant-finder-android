package com.umain.restaurantfinder.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umain.restaurantfinder.data.Filter
import com.umain.restaurantfinder.data.Restaurant
import com.umain.restaurantfinder.data.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RestaurantListUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val allRestaurants: List<Restaurant> = emptyList(),
    val filters: List<Filter> = emptyList(),
    val selectedFilterIds: Set<String> = emptySet()
) {
    val visibleRestaurants: List<Restaurant>
        get() = if (selectedFilterIds.isEmpty()) {
            allRestaurants
        } else {
            allRestaurants.filter { restaurant ->
                restaurant.filterIds.any { it in selectedFilterIds }
            }
        }
}

class RestaurantListViewModel(
    private val repository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantListUiState())
    val uiState: StateFlow<RestaurantListUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val restaurants = repository.getRestaurants()
                val filters = repository.getFiltersFor(restaurants)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        allRestaurants = restaurants,
                        filters = filters
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Could not load restaurants. Please try again."
                    )
                }
            }
        }
    }

    fun onFilterClicked(filterId: String) {
        _uiState.update { state ->
            val newSelected = if (filterId in state.selectedFilterIds) {
                state.selectedFilterIds - filterId
            } else {
                state.selectedFilterIds + filterId
            }
            state.copy(selectedFilterIds = newSelected)
        }
    }
}
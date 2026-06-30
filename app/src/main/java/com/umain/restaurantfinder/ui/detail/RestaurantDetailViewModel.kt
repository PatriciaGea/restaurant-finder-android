package com.umain.restaurantfinder.ui.detail

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

data class RestaurantDetailUiState(
    val restaurant: Restaurant? = null,
    val filters: List<Filter> = emptyList(),
    val isLoading: Boolean = true,
    val isOpen: Boolean? = null,
    val errorMessage: String? = null
)

class RestaurantDetailViewModel(
    private val repository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantDetailUiState())
    val uiState: StateFlow<RestaurantDetailUiState> = _uiState.asStateFlow()

    fun loadRestaurant(restaurantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val restaurant = repository.getCachedRestaurantById(restaurantId)
            if (restaurant == null) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Restaurant not found.")
                }
                return@launch
            }

            val relevantFilters = repository.getCachedFilters()
                .filter { it.id in restaurant.filterIds }

            _uiState.update { it.copy(restaurant = restaurant, filters = relevantFilters) }

            try {
                val status = repository.getOpenStatus(restaurantId)
                _uiState.update {
                    it.copy(isLoading = false, isOpen = status.isCurrentlyOpen)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Could not load status.")
                }
            }
        }
    }
}

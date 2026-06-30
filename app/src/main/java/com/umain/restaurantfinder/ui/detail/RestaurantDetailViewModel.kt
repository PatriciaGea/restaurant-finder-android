package com.umain.restaurantfinder.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umain.restaurantfinder.data.Restaurant
import com.umain.restaurantfinder.data.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RestaurantDetailUiState(
    val isLoading: Boolean = true,
    val isOpen: Boolean? = null,
    val errorMessage: String? = null
)

class RestaurantDetailViewModel(
    private val repository: RestaurantRepository = RestaurantRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantDetailUiState())
    val uiState: StateFlow<RestaurantDetailUiState> = _uiState.asStateFlow()

    fun loadOpenStatus(restaurantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
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
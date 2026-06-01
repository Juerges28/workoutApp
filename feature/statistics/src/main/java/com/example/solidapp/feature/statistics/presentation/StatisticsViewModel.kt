package com.example.solidapp.feature.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.solidapp.domain.model.WorkoutCategory
import com.example.solidapp.domain.usecase.CalculateMonthlyStatsUseCase
import com.example.solidapp.domain.usecase.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val calculateMonthlyStatsUseCase: CalculateMonthlyStatsUseCase
) : ViewModel() {

    private val _statsState = MutableStateFlow<Map<WorkoutCategory, Double>>(emptyMap())
    val statsState: StateFlow<Map<WorkoutCategory, Double>> = _statsState.asStateFlow()

    init { loadStatistics() }

    private fun loadStatistics() {
        viewModelScope.launch {
            getExpensesUseCase().collect { sessions ->
                _statsState.value = calculateMonthlyStatsUseCase(sessions)
            }
        }
    }
}

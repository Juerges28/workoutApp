package com.example.solidapp.feature.expenses.presentation

import com.example.solidapp.domain.model.WorkoutSession

data class ExpenseUiState(
    val sessions: List<WorkoutSession> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalMinutes: Double = 0.0,
    val titleError: String? = null,
    val durationError: String? = null,
    val categoryError: String? = null,
    val addSuccess: Boolean = false,
    val exportResult: String? = null
)

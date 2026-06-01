package com.example.solidapp.feature.expenses.presentation

import com.example.solidapp.domain.model.Expense

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalAmount: Double = 0.0,
    val titleError: String? = null,
    val amountError: String? = null,
    val categoryError: String? = null,
    val addSuccess: Boolean = false,
    val exportResult: String? = null
)

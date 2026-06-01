package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.Expense
import javax.inject.Inject

class CalculateTotalUseCase @Inject constructor() {
    operator fun invoke(expenses: List<Expense>): Double = expenses.sumOf { it.amount }
}

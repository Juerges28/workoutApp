package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.model.ExpenseCategory
import javax.inject.Inject

class CalculateMonthlyStatsUseCase @Inject constructor() {
    operator fun invoke(expenses: List<Expense>): Map<ExpenseCategory, Double> {
        return expenses
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }
}

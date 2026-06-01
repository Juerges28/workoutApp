package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.repository.ExpenseRepository
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        repository.deleteExpense(expense)
    }
}

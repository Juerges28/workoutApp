package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        if (expense.title.isBlank()) {
            throw IllegalArgumentException("Title cannot be empty")
        }
        if (expense.amount <= 0) {
            throw IllegalArgumentException("Amount must be greater than 0")
        }
        repository.insertExpense(expense)
    }
}

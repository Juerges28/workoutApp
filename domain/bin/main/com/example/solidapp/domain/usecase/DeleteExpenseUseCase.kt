package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.repository.ExpenseWriter
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val writer: ExpenseWriter
) {
    suspend operator fun invoke(expense: Expense) {
        writer.deleteExpense(expense)
    }
}

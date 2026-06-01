package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<Expense>> {
        return repository.getAllExpenses()
    }
}

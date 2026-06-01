package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.WorkoutSession
import com.example.solidapp.domain.repository.ExpenseReader
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val reader: ExpenseReader
) {
    operator fun invoke(): Flow<List<WorkoutSession>> = reader.getAllExpenses()
}

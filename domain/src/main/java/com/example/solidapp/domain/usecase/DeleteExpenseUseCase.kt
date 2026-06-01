package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.WorkoutSession
import com.example.solidapp.domain.repository.ExpenseWriter
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val writer: ExpenseWriter
) {
    suspend operator fun invoke(session: WorkoutSession) {
        writer.deleteExpense(session)
    }
}

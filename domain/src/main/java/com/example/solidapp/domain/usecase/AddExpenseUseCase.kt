package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.WorkoutCategory
import com.example.solidapp.domain.model.WorkoutLocation
import com.example.solidapp.domain.model.WorkoutSession
import com.example.solidapp.domain.repository.ExpenseWriter
import com.example.solidapp.domain.util.TimeProvider
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val writer: ExpenseWriter,
    private val timeProvider: TimeProvider
) {
    suspend operator fun invoke(
        title: String,
        durationMinutes: Double,
        category: WorkoutCategory,
        location: WorkoutLocation = WorkoutLocation.Gym
    ) {
        if (title.isBlank()) throw IllegalArgumentException("El nombre no puede estar vacío")
        if (durationMinutes <= 0) throw IllegalArgumentException("La duración debe ser mayor a 0")
        writer.insertExpense(
            WorkoutSession(
                title           = title,
                durationMinutes = durationMinutes,
                category        = category,
                timestamp       = timeProvider.now(),
                location        = location
            )
        )
    }
}

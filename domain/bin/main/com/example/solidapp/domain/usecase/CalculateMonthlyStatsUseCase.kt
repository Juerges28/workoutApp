package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.WorkoutCategory
import com.example.solidapp.domain.model.WorkoutSession
import javax.inject.Inject

class CalculateMonthlyStatsUseCase @Inject constructor() {
    operator fun invoke(sessions: List<WorkoutSession>): Map<WorkoutCategory, Double> {
        return sessions
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.durationMinutes } }
    }
}

package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.WorkoutSession
import javax.inject.Inject

class CalculateTotalUseCase @Inject constructor() {
    operator fun invoke(sessions: List<WorkoutSession>): Double =
        sessions.sumOf { it.durationMinutes }
}

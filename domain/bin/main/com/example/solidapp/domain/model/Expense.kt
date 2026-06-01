package com.example.solidapp.domain.model

data class WorkoutSession(
    val id: Long = 0,
    val title: String,
    val durationMinutes: Double,
    val category: WorkoutCategory,
    val timestamp: Long,
    val location: WorkoutLocation = WorkoutLocation.Gym
)

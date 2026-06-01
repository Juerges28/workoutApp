package com.example.solidapp.domain.model

sealed class WorkoutLocation {
    data object Gym : WorkoutLocation()
    data object Home : WorkoutLocation()
    data object Outdoor : WorkoutLocation()
}

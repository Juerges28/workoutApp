package com.example.solidapp.domain.export

import com.example.solidapp.domain.model.WorkoutSession

interface ExpenseExporter {
    fun export(sessions: List<WorkoutSession>): String
}

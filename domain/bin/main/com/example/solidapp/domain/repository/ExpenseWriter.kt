package com.example.solidapp.domain.repository

import com.example.solidapp.domain.model.WorkoutSession

interface ExpenseWriter {
    suspend fun insertExpense(session: WorkoutSession)
    suspend fun deleteExpense(session: WorkoutSession)
}

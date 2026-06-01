package com.example.solidapp.domain.repository

import com.example.solidapp.domain.model.WorkoutSession
import kotlinx.coroutines.flow.Flow

interface ExpenseReader {
    fun getAllExpenses(): Flow<List<WorkoutSession>>
    suspend fun getExpenseById(id: Long): WorkoutSession?
}

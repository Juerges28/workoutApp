package com.example.solidapp.domain.repository

import com.example.solidapp.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseReader {
    fun getAllExpenses(): Flow<List<Expense>>
    suspend fun getExpenseById(id: Long): Expense?
}

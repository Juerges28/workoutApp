package com.example.solidapp.domain.repository

import com.example.solidapp.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    suspend fun getExpenseById(id: Long): Expense?
    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
}

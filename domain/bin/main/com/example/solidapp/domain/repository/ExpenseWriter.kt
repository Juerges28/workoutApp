package com.example.solidapp.domain.repository

import com.example.solidapp.domain.model.Expense

interface ExpenseWriter {
    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
}

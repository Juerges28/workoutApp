package com.example.solidapp.domain.export

import com.example.solidapp.domain.model.Expense

interface ExpenseExporter {
    fun export(expenses: List<Expense>): String
}

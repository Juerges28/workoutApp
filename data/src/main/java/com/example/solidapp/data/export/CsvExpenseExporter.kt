package com.example.solidapp.data.export

import com.example.solidapp.domain.export.ExpenseExporter
import com.example.solidapp.domain.model.Expense

class CsvExpenseExporter : ExpenseExporter {
    override fun export(expenses: List<Expense>): String {
        val builder = StringBuilder()
        builder.append("ID,Title,Amount,Category,Timestamp\n")
        expenses.forEach {
            builder.append("${it.id},${it.title},${it.amount},${it.category.displayName},${it.timestamp}\n")
        }
        return builder.toString()
    }
}

package com.example.solidapp.data.export

import com.example.solidapp.domain.export.ExpenseExporter
import com.example.solidapp.domain.model.Expense

class PdfExpenseExporter : ExpenseExporter {
    override fun export(expenses: List<Expense>): String {
        // Simulated PDF Export
        val builder = java.lang.StringBuilder()
        builder.append("--- PDF DOCUMENT START ---\n")
        builder.append("EXPENSES REPORT\n\n")
        expenses.forEach {
            builder.append("* ${it.title}: $${it.amount} (${it.category.displayName})\n")
        }
        builder.append("--- PDF DOCUMENT END ---\n")
        return builder.toString()
    }
}

package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.export.ExpenseExporter
import com.example.solidapp.domain.model.Expense
import javax.inject.Inject

class ExportExpensesUseCase @Inject constructor(
    private val exporters: Map<String, @JvmSuppressWildcards ExpenseExporter>
) {
    operator fun invoke(expenses: List<Expense>, format: String): String {
        val exporter = exporters[format] ?: throw IllegalArgumentException("Unsupported format: $format")
        return exporter.export(expenses)
    }
}

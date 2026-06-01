package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.export.ExpenseExporter
import com.example.solidapp.domain.model.WorkoutSession
import javax.inject.Inject

class ExportExpensesUseCase @Inject constructor(
    private val exporters: Map<String, @JvmSuppressWildcards ExpenseExporter>
) {
    operator fun invoke(sessions: List<WorkoutSession>, format: String): String {
        val exporter = exporters[format]
            ?: throw IllegalArgumentException("Formato no soportado: $format")
        return exporter.export(sessions)
    }
}

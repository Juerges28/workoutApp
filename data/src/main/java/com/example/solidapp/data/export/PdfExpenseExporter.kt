package com.example.solidapp.data.export

import com.example.solidapp.domain.export.ExpenseExporter
import com.example.solidapp.domain.model.WorkoutSession

class PdfExpenseExporter : ExpenseExporter {
    override fun export(sessions: List<WorkoutSession>): String {
        val builder = StringBuilder()
        builder.append("--- REPORTE DE ENTRENAMIENTO ---\n\n")
        sessions.forEach {
            builder.append("• ${it.title} — ${it.durationMinutes.toInt()} min " +
                           "(${it.category.displayName})\n")
        }
        val total = sessions.sumOf { it.durationMinutes }
        builder.append("\nTotal: ${total.toInt()} minutos en ${sessions.size} sesiones\n")
        builder.append("--------------------------------\n")
        return builder.toString()
    }
}

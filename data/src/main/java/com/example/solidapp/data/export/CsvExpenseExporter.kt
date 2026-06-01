package com.example.solidapp.data.export

import com.example.solidapp.domain.export.ExpenseExporter
import com.example.solidapp.domain.model.WorkoutSession

class CsvExpenseExporter : ExpenseExporter {
    override fun export(sessions: List<WorkoutSession>): String {
        val builder = StringBuilder()
        builder.append("ID,Ejercicio,Duración(min),Tipo,Lugar,Fecha\n")
        sessions.forEach {
            builder.append("${it.id},${it.title},${it.durationMinutes}," +
                           "${it.category.displayName},${it.location::class.simpleName},${it.timestamp}\n")
        }
        return builder.toString()
    }
}

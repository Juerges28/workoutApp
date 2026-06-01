package com.example.solidapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.solidapp.domain.model.Expense

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val timestamp: Long
)

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        category = category,
        timestamp = timestamp
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category,
        timestamp = timestamp
    )
}

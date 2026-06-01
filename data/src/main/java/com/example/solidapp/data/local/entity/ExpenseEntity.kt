package com.example.solidapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.model.ExpenseCategory
import com.example.solidapp.domain.model.PaymentMethod

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val timestamp: Long,
    val paymentMethodType: String = "Cash",
    val cardLastFourDigits: String? = null
)

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        category = runCatching { ExpenseCategory.valueOf(category) }.getOrDefault(ExpenseCategory.OTHER),
        timestamp = timestamp,
        paymentMethod = if (paymentMethodType == "Card") {
            PaymentMethod.Card(cardLastFourDigits ?: "")
        } else {
            PaymentMethod.Cash
        }
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category.name,
        timestamp = timestamp,
        paymentMethodType = when (paymentMethod) {
            is PaymentMethod.Cash -> "Cash"
            is PaymentMethod.Card -> "Card"
        },
        cardLastFourDigits = when (val m = paymentMethod) {
            is PaymentMethod.Card -> m.lastFourDigits
            else -> null
        }
    )
}

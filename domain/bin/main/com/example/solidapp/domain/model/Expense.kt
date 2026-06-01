package com.example.solidapp.domain.model

data class Expense(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val timestamp: Long,
    val paymentMethod: PaymentMethod = PaymentMethod.Cash
)

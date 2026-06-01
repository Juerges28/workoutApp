package com.example.solidapp.domain.usecase

import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.model.ExpenseCategory
import com.example.solidapp.domain.model.PaymentMethod
import com.example.solidapp.domain.repository.ExpenseWriter
import com.example.solidapp.domain.util.TimeProvider
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val writer: ExpenseWriter,
    private val timeProvider: TimeProvider
) {
    suspend operator fun invoke(
        title: String,
        amount: Double,
        category: ExpenseCategory,
        paymentMethod: PaymentMethod = PaymentMethod.Cash
    ) {
        if (title.isBlank()) throw IllegalArgumentException("El título no puede estar vacío")
        if (amount <= 0) throw IllegalArgumentException("El monto debe ser mayor a 0")
        writer.insertExpense(
            Expense(
                title = title,
                amount = amount,
                category = category,
                timestamp = timeProvider.now(),
                paymentMethod = paymentMethod
            )
        )
    }
}

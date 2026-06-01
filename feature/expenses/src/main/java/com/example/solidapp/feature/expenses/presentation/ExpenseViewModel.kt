package com.example.solidapp.feature.expenses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.model.ExpenseCategory
import com.example.solidapp.domain.model.PaymentMethod
import com.example.solidapp.domain.usecase.AddExpenseUseCase
import com.example.solidapp.domain.usecase.CalculateTotalUseCase
import com.example.solidapp.domain.usecase.DeleteExpenseUseCase
import com.example.solidapp.domain.usecase.ExportExpensesUseCase
import com.example.solidapp.domain.usecase.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val calculateTotalUseCase: CalculateTotalUseCase,
    private val exportExpensesUseCase: ExportExpensesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseUiState())
    val state: StateFlow<ExpenseUiState> = _state.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        getExpensesUseCase()
            .onEach { expenses ->
                _state.update {
                    it.copy(
                        expenses = expenses,
                        totalAmount = calculateTotalUseCase(expenses),
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
            .launchIn(viewModelScope)
    }

    fun addExpense(title: String, amountText: String, category: ExpenseCategory?, paymentMethod: PaymentMethod) {
        val amount = amountText.toDoubleOrNull()
        val titleErr = if (title.isBlank()) "El título no puede estar vacío" else null
        val amountErr = if (amount == null || amount <= 0) "Introduce una cantidad válida" else null
        val categoryErr = if (category == null) "Selecciona una categoría" else null

        if (titleErr != null || amountErr != null || categoryErr != null) {
            _state.update { it.copy(titleError = titleErr, amountError = amountErr, categoryError = categoryErr) }
            return
        }

        viewModelScope.launch {
            try {
                addExpenseUseCase(title, amount!!, category!!, paymentMethod)
                _state.update { it.copy(titleError = null, amountError = null, categoryError = null, addSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun clearAddSuccess() {
        _state.update { it.copy(addSuccess = false) }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                deleteExpenseUseCase(expense)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun exportExpenses(format: String) {
        viewModelScope.launch {
            try {
                val result = exportExpensesUseCase(_state.value.expenses, format)
                _state.update { it.copy(exportResult = result) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun clearExportResult() {
        _state.update { it.copy(exportResult = null) }
    }
}

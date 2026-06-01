package com.example.solidapp.feature.expenses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.solidapp.domain.model.Expense
import com.example.solidapp.domain.usecase.AddExpenseUseCase
import com.example.solidapp.domain.usecase.DeleteExpenseUseCase
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
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseUiState())
    val state: StateFlow<ExpenseUiState> = _state.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        getExpensesUseCase()
            .onEach { expenses ->
                val total = expenses.sumOf { it.amount }
                _state.update { 
                    it.copy(
                        expenses = expenses, 
                        totalAmount = total,
                        isLoading = false,
                        error = null
                    ) 
                }
            }
            .catch { e ->
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Unknown error"
                    ) 
                }
            }
            .launchIn(viewModelScope)
    }

    fun addExpense(title: String, amount: Double, category: String) {
        viewModelScope.launch {
            try {
                val expense = Expense(
                    title = title,
                    amount = amount,
                    category = category,
                    timestamp = System.currentTimeMillis()
                )
                addExpenseUseCase(expense)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage) }
            }
        }
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
}

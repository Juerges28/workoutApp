package com.example.solidapp.feature.expenses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.solidapp.domain.model.WorkoutCategory
import com.example.solidapp.domain.model.WorkoutLocation
import com.example.solidapp.domain.model.WorkoutSession
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

    init { loadSessions() }

    private fun loadSessions() {
        getExpensesUseCase()
            .onEach { sessions ->
                _state.update {
                    it.copy(
                        sessions     = sessions,
                        totalMinutes = calculateTotalUseCase(sessions),
                        isLoading    = false,
                        error        = null
                    )
                }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Error desconocido") }
            }
            .launchIn(viewModelScope)
    }

    fun addSession(title: String, durationText: String, category: WorkoutCategory?, location: WorkoutLocation) {
        val duration = durationText.toDoubleOrNull()
        val titleErr    = if (title.isBlank())                    "El nombre no puede estar vacío"  else null
        val durationErr = if (duration == null || duration <= 0)  "Ingresa una duración válida"      else null
        val categoryErr = if (category == null)                   "Selecciona el tipo"               else null

        if (titleErr != null || durationErr != null || categoryErr != null) {
            _state.update { it.copy(titleError = titleErr, durationError = durationErr, categoryError = categoryErr) }
            return
        }
        viewModelScope.launch {
            try {
                addExpenseUseCase(title, duration!!, category!!, location)
                _state.update { it.copy(titleError = null, durationError = null, categoryError = null, addSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun clearAddSuccess() = _state.update { it.copy(addSuccess = false) }

    fun deleteSession(session: WorkoutSession) {
        viewModelScope.launch {
            try { deleteExpenseUseCase(session) }
            catch (e: Exception) { _state.update { it.copy(error = e.localizedMessage) } }
        }
    }

    fun exportSessions(format: String) {
        viewModelScope.launch {
            try {
                val result = exportExpensesUseCase(_state.value.sessions, format)
                _state.update { it.copy(exportResult = result) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun clearExportResult() = _state.update { it.copy(exportResult = null) }
}

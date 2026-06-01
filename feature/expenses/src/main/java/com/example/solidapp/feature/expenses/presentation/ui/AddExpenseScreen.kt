package com.example.solidapp.feature.expenses.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.solidapp.core.ui.components.PrimaryButton
import com.example.solidapp.core.ui.components.SolidTextField
import com.example.solidapp.domain.model.ExpenseCategory
import com.example.solidapp.domain.model.PaymentMethod
import com.example.solidapp.feature.expenses.presentation.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ExpenseCategory?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var paymentMethod by remember { mutableStateOf<PaymentMethod>(PaymentMethod.Cash) }
    var cardDigits by remember { mutableStateOf("") }

    LaunchedEffect(state.addSuccess) {
        if (state.addSuccess) {
            viewModel.clearAddSuccess()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Gasto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SolidTextField(
                value = title,
                onValueChange = { title = it },
                label = "Título (ej. Supermercado)",
                isError = state.titleError != null,
                errorMessage = state.titleError
            )

            SolidTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = "Cantidad",
                isError = state.amountError != null,
                errorMessage = state.amountError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory?.displayName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    isError = state.categoryError != null,
                    supportingText = { if (state.categoryError != null) Text(state.categoryError!!) }
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    ExpenseCategory.entries.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.displayName) },
                            onClick = {
                                selectedCategory = cat
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            Text("Método de pago", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = paymentMethod is PaymentMethod.Cash,
                    onClick = { paymentMethod = PaymentMethod.Cash },
                    label = { Text("Efectivo") }
                )
                FilterChip(
                    selected = paymentMethod is PaymentMethod.Card,
                    onClick = { paymentMethod = PaymentMethod.Card(cardDigits) },
                    label = { Text("Tarjeta") }
                )
            }

            if (paymentMethod is PaymentMethod.Card) {
                SolidTextField(
                    value = cardDigits,
                    onValueChange = {
                        if (it.length <= 4) {
                            cardDigits = it
                            paymentMethod = PaymentMethod.Card(it)
                        }
                    },
                    label = "Últimos 4 dígitos",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            PrimaryButton(
                text = "Guardar Gasto",
                onClick = {
                    viewModel.addExpense(title, amountText, selectedCategory, paymentMethod)
                }
            )
        }
    }
}

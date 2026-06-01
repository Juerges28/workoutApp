package com.example.solidapp.feature.expenses.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.solidapp.feature.expenses.presentation.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { 
                    title = it 
                    titleError = it.isBlank()
                },
                label = { Text("Título (ej. Supermercado)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = titleError,
                supportingText = { if (titleError) Text("El título no puede estar vacío") }
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { 
                    amount = it 
                    amountError = it.toDoubleOrNull() == null || it.toDouble() <= 0
                },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                isError = amountError,
                supportingText = { if (amountError) Text("Introduce una cantidad válida") }
            )

            OutlinedTextField(
                value = category,
                onValueChange = { 
                    category = it 
                    categoryError = it.isBlank()
                },
                label = { Text("Categoría (ej. Comida, Transporte)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = categoryError,
                supportingText = { if (categoryError) Text("La categoría no puede estar vacía") }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    
                    titleError = title.isBlank()
                    amountError = amountDouble == null || amountDouble <= 0
                    categoryError = category.isBlank()

                    if (!titleError && !amountError && !categoryError && amountDouble != null) {
                        viewModel.addExpense(title, amountDouble, category)
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Guardar Gasto")
            }
        }
    }
}

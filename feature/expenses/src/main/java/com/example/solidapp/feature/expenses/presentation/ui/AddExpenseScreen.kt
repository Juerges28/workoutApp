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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.solidapp.core.ui.components.SolidTextField
import com.example.solidapp.domain.model.WorkoutCategory
import com.example.solidapp.domain.model.WorkoutLocation
import com.example.solidapp.feature.expenses.presentation.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var title            by remember { mutableStateOf("") }
    var durationText     by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<WorkoutCategory?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var location         by remember { mutableStateOf<WorkoutLocation>(WorkoutLocation.Gym) }

    LaunchedEffect(state.addSuccess) {
        if (state.addSuccess) { viewModel.clearAddSuccess(); onNavigateBack() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva sesión", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor             = MaterialTheme.colorScheme.background,
                    titleContentColor          = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            SolidTextField(
                value         = title,
                onValueChange = { title = it },
                label         = "Nombre del ejercicio",
                isError       = state.titleError != null,
                errorMessage  = state.titleError
            )

            SolidTextField(
                value           = durationText,
                onValueChange   = { durationText = it },
                label           = "Duración (min)",
                isError         = state.durationError != null,
                errorMessage    = state.durationError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            ExposedDropdownMenuBox(
                expanded         = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                OutlinedTextField(
                    value          = selectedCategory?.displayName ?: "",
                    onValueChange  = {},
                    readOnly       = true,
                    label          = { Text("Tipo") },
                    trailingIcon   = { Icon(Icons.Default.ArrowDropDown, null) },
                    modifier       = Modifier.fillMaxWidth().menuAnchor(),
                    isError        = state.categoryError != null,
                    supportingText = { if (state.categoryError != null) Text(state.categoryError!!) }
                )
                ExposedDropdownMenu(
                    expanded         = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    WorkoutCategory.entries.forEach { cat ->
                        DropdownMenuItem(
                            text    = { Text(cat.displayName) },
                            onClick = { selectedCategory = cat; categoryExpanded = false }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Lugar",
                    style      = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        "Gimnasio" to WorkoutLocation.Gym,
                        "Casa"     to WorkoutLocation.Home,
                        "Exterior" to WorkoutLocation.Outdoor
                    ).forEach { (label, loc) ->
                        FilterChip(
                            selected = location == loc,
                            onClick  = { location = loc },
                            label    = { Text(label, style = MaterialTheme.typography.labelMedium) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.addSession(title, durationText, selectedCategory, location) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = MaterialTheme.shapes.medium
            ) {
                Text("Guardar sesión", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

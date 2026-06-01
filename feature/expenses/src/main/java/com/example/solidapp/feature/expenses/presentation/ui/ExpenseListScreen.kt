package com.example.solidapp.feature.expenses.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.solidapp.domain.model.WorkoutCategory
import com.example.solidapp.domain.model.WorkoutLocation
import com.example.solidapp.domain.model.WorkoutSession
import com.example.solidapp.feature.expenses.presentation.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    onNavigateToAddExpense: () -> Unit,
    onNavigateToStatistics: () -> Unit = {},
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showExportDialog by remember { mutableStateOf(false) }

    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Exportar sesiones") },
            text  = { Text("Elige el formato:") },
            confirmButton = {
                TextButton(onClick = { viewModel.exportSessions("CSV"); showExportDialog = false }) {
                    Text("CSV")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.exportSessions("PDF"); showExportDialog = false }) {
                    Text("PDF")
                }
            }
        )
    }

    state.exportResult?.let { result ->
        AlertDialog(
            onDismissRequest = { viewModel.clearExportResult() },
            title = { Text("Exportación") },
            text  = {
                Text(
                    text       = result,
                    style      = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.clearExportResult() }) { Text("Cerrar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrenamiento", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(Icons.Default.Share, contentDescription = "Exportar",
                             tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = onNavigateToStatistics) {
                        Icon(Icons.Default.BarChart, contentDescription = "Estadísticas",
                             tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = onNavigateToAddExpense,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary,
                shape          = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Sesión")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Minimal header — solo números grandes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text  = "${state.totalMinutes.toInt()}",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize   = 64.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text  = "minutos totales",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (state.sessions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text  = "${state.sessions.size} sesiones",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            if (state.error != null) {
                Text(
                    text     = state.error ?: "",
                    color    = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp),
                    style    = MaterialTheme.typography.bodySmall
                )
            }

            if (state.sessions.isEmpty() && !state.isLoading) {
                EmptyStateView()
            } else {
                LazyColumn(
                    modifier       = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = state.sessions, key = { it.id }) { session ->
                        SwipeToDeleteItem(
                            session  = session,
                            onDelete = { viewModel.deleteSession(session) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier            = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text      = "Sin sesiones",
            style     = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color     = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text      = "Toca + para registrar tu primera sesión.",
            style     = MaterialTheme.typography.bodySmall,
            color     = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteItem(session: WorkoutSession, onDelete: () -> Unit) {
    var isDeleted by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                isDeleted = true; onDelete(); true
            } else false
        }
    )

    AnimatedVisibility(
        visible = !isDeleted,
        exit    = shrinkVertically(animationSpec = tween(250)) + fadeOut()
    ) {
        SwipeToDismissBox(
            state                      = dismissState,
            enableDismissFromStartToEnd = false,
            backgroundContent = {
                val active = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
                Box(
                    modifier         = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) MaterialTheme.colorScheme.error else Color.Transparent)
                        .padding(end = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (active) {
                        Icon(Icons.Default.Delete, contentDescription = null,
                             tint = MaterialTheme.colorScheme.onError)
                    }
                }
            }
        ) {
            SessionItem(session = session)
        }
    }
}

@Composable
fun SessionItem(session: WorkoutSession) {
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    val categoryIcon = when (session.category) {
        WorkoutCategory.CARDIO      -> Icons.Default.DirectionsRun
        WorkoutCategory.STRENGTH    -> Icons.Default.FitnessCenter
        WorkoutCategory.FLEXIBILITY -> Icons.Default.SelfImprovement
        WorkoutCategory.SPORTS      -> Icons.Default.SportsSoccer
        WorkoutCategory.REST        -> Icons.Default.Bedtime
        WorkoutCategory.OTHER       -> Icons.Default.MoreHoriz
    }

    val locationLabel = when (session.location) {
        is WorkoutLocation.Gym     -> "Gimnasio"
        is WorkoutLocation.Home    -> "Casa"
        is WorkoutLocation.Outdoor -> "Exterior"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector        = categoryIcon,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier           = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text       = session.title,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text  = "${session.category.displayName}  ·  $locationLabel  ·  ${dateFormat.format(Date(session.timestamp))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text       = "${session.durationMinutes.toInt()}",
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text  = "min",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

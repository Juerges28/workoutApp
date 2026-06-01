package com.example.solidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.solidapp.core.ui.theme.SolidAppTheme
import com.example.solidapp.feature.expenses.presentation.ui.AddExpenseScreen
import com.example.solidapp.feature.expenses.presentation.ui.ExpenseListScreen
import com.example.solidapp.feature.statistics.presentation.StatisticsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SolidAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "expense_list") {
                        composable("expense_list") {
                            ExpenseListScreen(
                                onNavigateToAddExpense = { navController.navigate("add_expense") },
                                onNavigateToStatistics = { navController.navigate("statistics") }
                            )
                        }
                        composable("add_expense") {
                            AddExpenseScreen(onNavigateBack = { navController.popBackStack() })
                        }
                        composable("statistics") {
                            StatisticsScreen(onNavigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

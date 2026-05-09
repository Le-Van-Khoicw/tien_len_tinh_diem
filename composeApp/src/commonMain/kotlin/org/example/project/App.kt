package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.ui.screen.MainGameScreen
import org.example.project.ui.screen.MatchHistoryScreen
import org.example.project.ui.screen.SetupScreen
import org.example.project.viewmodel.GameViewModel

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val viewModel: GameViewModel = viewModel()

        NavHost(
            navController = navController,
            startDestination = "match_history"
        ) {
            composable(route = "match_history") {
                MatchHistoryScreen(
                    viewModel = viewModel,
                    onAddNewMatch = {
                        navController.navigate("setup")
                    }
                )
            }

            composable(route = "setup") {
                SetupScreen(
                    viewModel = viewModel,
                    onStartGame = {
                        navController.navigate("game") {
                            popUpTo("match_history") 
                        }
                    },
                    onDismiss = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = "game") {
                MainGameScreen(
                    viewModel = viewModel,
                    onExitGame = {
                        navController.navigate("match_history") {
                            popUpTo("match_history") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

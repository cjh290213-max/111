package com.example.gamepicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamepicker.ui.screens.GameListScreen
import com.example.gamepicker.ui.screens.GamePickerScreen
import com.example.gamepicker.viewmodel.GameViewModel

@Composable
fun GamePickerApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "game_list",
        modifier = modifier
    ) {
        composable("game_list") {
            GameListScreen(
                viewModel = viewModel,
                onPickGame = {
                    navController.navigate("game_picker")
                }
            )
        }
        composable("game_picker") {
            GamePickerScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

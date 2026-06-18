package com.example.gamepicker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamepicker.data.Game
import com.example.gamepicker.data.GameDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val gameDao = GameDatabase.getDatabase(application).gameDao()

    val allGames: Flow<List<Game>> = gameDao.getAllGames()

    private val _pickedGame = MutableStateFlow<Game?>(null)
    val pickedGame: StateFlow<Game?> = _pickedGame.asStateFlow()

    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning.asStateFlow()

    fun addGame(name: String) {
        viewModelScope.launch {
            val trimmed = name.trim()
            if (trimmed.isNotEmpty()) {
                gameDao.insert(Game(name = trimmed))
            }
        }
    }

    fun deleteGame(game: Game) {
        viewModelScope.launch {
            gameDao.delete(game)
        }
    }

    fun pickRandomGame() {
        viewModelScope.launch {
            _isSpinning.value = true

            // Simulate spinning animation delay
            kotlinx.coroutines.delay(1500)

            val random = gameDao.getRandomGame()
            _pickedGame.value = random
            _isSpinning.value = false
        }
    }

    fun resetPickedGame() {
        _pickedGame.value = null
    }
}

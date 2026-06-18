package com.example.gamepicker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val addedTime: Long = System.currentTimeMillis()
)

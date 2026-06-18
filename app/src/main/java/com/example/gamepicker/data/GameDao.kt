package com.example.gamepicker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY addedTime DESC")
    fun getAllGames(): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game): Long

    @Delete
    suspend fun delete(game: Game)

    @Query("SELECT * FROM games ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomGame(): Game?
}

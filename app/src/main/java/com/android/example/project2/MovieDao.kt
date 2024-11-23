package com.android.example.project2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMovie(movies: List<Movies>)

    @Query("SELECT * FROM MOVIES_TABLE")
    fun getAllMovies():Flow<List<Movies>>
}
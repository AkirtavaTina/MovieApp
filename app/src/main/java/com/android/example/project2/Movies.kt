package com.android.example.project2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies_table")
data class Movies(
       @SerializedName("poster_path") @ColumnInfo("poster_path") val path: String,
       @SerializedName("original_name") @PrimaryKey @ColumnInfo("original_name") val name: String,
       val overview: String,
       @SerializedName("vote_average") @ColumnInfo("vote_average") val vote: Float
)

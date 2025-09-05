package com.example.youtubeapp.data.local.entities

import androidx.room.*

@Entity(
    tableName = "watch_history",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("videoId")]
)
data class WatchHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int,
    val videoId: String,
    val watchedAt: Long = System.currentTimeMillis(),
    val progressSeconds: Int? = null,
    val durationSeconds: Int? = null
)

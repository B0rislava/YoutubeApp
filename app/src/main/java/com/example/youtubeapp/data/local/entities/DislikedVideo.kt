package com.example.youtubeapp.data.local.entities

import androidx.room.*

@Entity(
    tableName = "disliked_videos",
    primaryKeys = ["userId", "videoId"],
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
data class DislikedVideo(
    val userId: Int,
    val videoId: String,
    val createdAt: Long = System.currentTimeMillis()
)

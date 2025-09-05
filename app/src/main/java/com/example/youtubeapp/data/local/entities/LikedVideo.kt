package com.example.youtubeapp.data.local.entities

import androidx.room.*

@Entity(
    tableName = "liked_videos",
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
data class LikedVideo(
    val userId: Int,
    val videoId: String,
    val createdAt: Long = System.currentTimeMillis()
)

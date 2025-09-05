package com.example.youtubeapp.data.local.entities

import androidx.room.*

@Entity(
    tableName = "subscribed_channels",
    primaryKeys = ["userId", "channelId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("channelId")]
)
data class SubscribedChannel(
    val userId: Int,
    val channelId: String,
    val createdAt: Long = System.currentTimeMillis()
)

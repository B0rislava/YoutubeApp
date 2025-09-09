package com.example.youtubeapp.data.local.dao

import androidx.room.*
import com.example.youtubeapp.data.local.entities.SubscribedChannel

@Dao
interface SubscriptionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun subscribe(entry: SubscribedChannel)

    @Query("DELETE FROM subscribed_channels WHERE userId = :userId AND channelId = :channelId")
    suspend fun unsubscribe(userId: Int, channelId: String)

    @Query("SELECT channelId FROM subscribed_channels WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getSubscriptions(userId: Int): List<String>

    @Query("DELETE FROM subscribed_channels WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Int)
}

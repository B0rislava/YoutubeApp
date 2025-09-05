package com.example.youtubeapp.data.local.dao

import androidx.room.*
import com.example.youtubeapp.data.local.entities.DislikedVideo

@Dao
interface DislikesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDislike(entry: DislikedVideo)

    @Query("DELETE FROM disliked_videos WHERE userId = :userId AND videoId = :videoId")
    suspend fun removeDislike(userId: Int, videoId: String)

    @Query("SELECT videoId FROM disliked_videos WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getDisliked(userId: Int): List<String>
}

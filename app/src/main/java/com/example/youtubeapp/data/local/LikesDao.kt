package com.example.youtubeapp.data.local.dao

import androidx.room.*
import com.example.youtubeapp.data.local.entities.LikedVideo

@Dao
interface LikesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLike(entry: LikedVideo)

    @Query("DELETE FROM liked_videos WHERE userId = :userId AND videoId = :videoId")
    suspend fun removeLike(userId: Int, videoId: String)

    @Query("SELECT videoId FROM liked_videos WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getLiked(userId: Int): List<String>

    @Query("DELETE FROM liked_videos WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Int)
}

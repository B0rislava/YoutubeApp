package com.example.youtubeapp.data.local.dao

import androidx.room.*
import com.example.youtubeapp.data.local.entities.WatchHistory

@Dao
interface HistoryDaoSimple {
    @Insert
    suspend fun addHistory(entry: WatchHistory)

    @Query("""
        SELECT videoId FROM watch_history 
        WHERE userId = :userId 
        ORDER BY watchedAt DESC 
        LIMIT :limit
    """)
    suspend fun getHistoryIds(userId: Int, limit: Int = 100): List<String>

    @Query("DELETE FROM watch_history WHERE userId = :userId AND videoId = :videoId")
    suspend fun deleteFromHistory(userId: Int, videoId: String)

    @Query("DELETE FROM watch_history WHERE userId = :userId")
    suspend fun clearHistory(userId: Int)
}

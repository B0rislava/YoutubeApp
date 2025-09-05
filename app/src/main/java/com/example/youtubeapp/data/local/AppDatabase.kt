package com.example.youtubeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.youtubeapp.data.local.dao.*
import com.example.youtubeapp.data.local.entities.*

@Database(
    entities = [
        User::class,
        LikedVideo::class,
        DislikedVideo::class,
        SubscribedChannel::class,
        WatchHistory::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun likesDao(): LikesDao
    abstract fun dislikesDao(): DislikesDao
    abstract fun subscriptionsDao(): SubscriptionsDao
    abstract fun historyDao(): HistoryDaoSimple
}

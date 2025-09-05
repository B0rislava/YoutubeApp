package com.example.youtubeapp.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {
    @Volatile private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase =
        instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "youtube_app_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build().also { instance = it }
        }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS liked_videos(
                    userId INTEGER NOT NULL,
                    videoId TEXT NOT NULL,
                    createdAt INTEGER NOT NULL,
                    PRIMARY KEY(userId, videoId),
                    FOREIGN KEY(userId) REFERENCES users(uid) ON DELETE CASCADE
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_likes_user ON liked_videos(userId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_likes_video ON liked_videos(videoId)")

            db.execSQL("""
                CREATE TABLE IF NOT EXISTS disliked_videos(
                    userId INTEGER NOT NULL,
                    videoId TEXT NOT NULL,
                    createdAt INTEGER NOT NULL,
                    PRIMARY KEY(userId, videoId),
                    FOREIGN KEY(userId) REFERENCES users(uid) ON DELETE CASCADE
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_dislikes_user ON disliked_videos(userId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_dislikes_video ON disliked_videos(videoId)")

            db.execSQL("""
                CREATE TABLE IF NOT EXISTS subscribed_channels(
                    userId INTEGER NOT NULL,
                    channelId TEXT NOT NULL,
                    createdAt INTEGER NOT NULL,
                    PRIMARY KEY(userId, channelId),
                    FOREIGN KEY(userId) REFERENCES users(uid) ON DELETE CASCADE
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_subs_user ON subscribed_channels(userId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_subs_channel ON subscribed_channels(channelId)")

            db.execSQL("""
                CREATE TABLE IF NOT EXISTS watch_history(
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    userId INTEGER NOT NULL,
                    videoId TEXT NOT NULL,
                    watchedAt INTEGER NOT NULL,
                    progressSeconds INTEGER,
                    durationSeconds INTEGER,
                    FOREIGN KEY(userId) REFERENCES users(uid) ON DELETE CASCADE
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_hist_user ON watch_history(userId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_hist_video ON watch_history(videoId)")
        }
    }
}

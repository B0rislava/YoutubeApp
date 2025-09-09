package com.example.youtubeapp.repository

import com.example.youtubeapp.data.local.dao.*
import com.example.youtubeapp.data.local.entities.*

class SimpleListsRepository(
    private val likes: LikesDao,
    private val dislikes: DislikesDao,
    private val subs: SubscriptionsDao,
    private val history: HistoryDaoSimple
) {
    // Likes / Dislikes (mutually exclusive)
    suspend fun like(userId: Int, videoId: String) {
        dislikes.removeDislike(userId, videoId)
        likes.addLike(LikedVideo(userId, videoId))
    }
    suspend fun dislike(userId: Int, videoId: String) {
        likes.removeLike(userId, videoId)
        dislikes.addDislike(DislikedVideo(userId, videoId))
    }
    suspend fun removeLike(userId: Int, videoId: String) = likes.removeLike(userId, videoId)
    suspend fun removeDislike(userId: Int, videoId: String) = dislikes.removeDislike(userId, videoId)
    suspend fun getLiked(userId: Int): List<String> = likes.getLiked(userId)
    suspend fun getDisliked(userId: Int): List<String> = dislikes.getDisliked(userId)

    // Subscriptions
    suspend fun subscribe(userId: Int, channelId: String) =
        subs.subscribe(SubscribedChannel(userId, channelId))
    suspend fun unsubscribe(userId: Int, channelId: String) =
        subs.unsubscribe(userId, channelId)
    suspend fun getSubscriptions(userId: Int): List<String> =
        subs.getSubscriptions(userId)

    // History
    suspend fun addToHistory(
        userId: Int,
        videoId: String,
        progress: Int? = null,
        duration: Int? = null
    ) = history.addHistory(
        WatchHistory(userId = userId, videoId = videoId, progressSeconds = progress, durationSeconds = duration)
    )
    suspend fun getHistory(userId: Int, limit: Int = 100): List<String> =
        history.getHistoryIds(userId, limit)
    suspend fun clearHistory(userId: Int) = history.clearHistory(userId)

    suspend fun clearForUser(uid: Int) {
        likes.deleteAllForUser(uid)
        dislikes.deleteAllForUser(uid)
        subs.deleteAllForUser(uid)
        history.deleteAllForUser(uid)
    }

}

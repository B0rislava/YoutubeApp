package com.example.youtubeapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.youtubeapp.data.local.DatabaseProvider
import com.example.youtubeapp.model.VideoItem
import com.example.youtubeapp.model.VideoRepository
import com.example.youtubeapp.model.VideoUser
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Composable
fun SubscriptionsScreen(
    currentUserEmail: String,
    navController: NavController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val userRepo = remember { com.example.youtubeapp.repository.UserRepository(db.userDao()) }
    val listsRepo = remember {
        com.example.youtubeapp.repository.SimpleListsRepository(
            likes = db.likesDao(),
            dislikes = db.dislikesDao(),
            subs = db.subscriptionsDao(),
            history = db.historyDao()
        )
    }

    var subscribedChannels by remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()

    // Load subscriptions
    LaunchedEffect(Unit) {
        scope.launch {
            val user = userRepo.getUserByEmail(currentUserEmail)
            val uid = user?.uid
            if (uid != null) {
                subscribedChannels = listsRepo.getSubscriptions(uid)
            }
        }
    }

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp)
    ) {
        if (subscribedChannels.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No subscriptions yet.")
            }
        } else {
            subscribedChannels.forEach { channel ->
                Text(
                    text = channel,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                val videos = VideoRepository.videos.filter { it.channel == channel }

                if (videos.isEmpty()) {
                    Text(
                        "No videos found for $channel",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                    )
                } else {
                    videos.forEach { video ->
                        SubscribedVideoItem(video = video, navController, currentUserEmail)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SubscribedVideoItem(
    video: VideoItem,
    navController: NavController,
    currentUserEmail: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("watch/${video.id}/$currentUserEmail") }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        GlideImage(
            imageModel = { VideoUser.getThumbnail(video.id) },
            modifier = Modifier
                .width(150.dp)
                .height(84.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(video.title, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(Modifier.height(4.dp))
            Text(
                video.channel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

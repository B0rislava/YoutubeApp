package com.example.youtubeapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.youtubeapp.R
import com.example.youtubeapp.components.MainBottomBar
import com.example.youtubeapp.model.VideoItem
import com.example.youtubeapp.model.VideoRepository
import com.example.youtubeapp.model.VideoUser
import com.example.youtubeapp.navigation.Screen
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun VideoWatchScreen(
    video: VideoItem,
    navController: NavController,
    currentUserEmail: String   // <— pass email so bottom bar can navigate
) {
    val scroll = rememberScrollState()

    Scaffold(
        bottomBar = {
            MainBottomBar(
                currentScreen = "Home" // keep Home highlighted
            ) { screen ->
                val tab = when (screen) {
                    "Home" -> 0
                    "Subscriptions" -> 1
                    "Profile" -> 2
                    else -> 0
                }
                navController.navigate("${Screen.Home.route}/$currentUserEmail?tab=$tab") {
                    launchSingleTop = true
                    restoreState = true
                    // optional: clear intermediate watch screens if you keep bouncing around
                    // popUpTo("${Screen.Home.route}/$currentUserEmail?tab=$tab") { inclusive = false }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scroll)
        ) {
            // Player
            VideoUser.Player(
                videoId = video.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            )

            // Title + meta + actions
            Column(Modifier.padding(12.dp)) {
                Text(video.title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(video.channel, style = MaterialTheme.typography.bodyMedium)
                    }
                    FilledTonalButton(onClick = { /* subscribe */ }) { Text("Subscribe") }
                }

                Spacer(Modifier.height(12.dp))

                // ——— Action buttons @ 30% scale ———
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ActionChipSmall(R.drawable.thumbs_up,   "Like")   { /* like */ }
                    ActionChipSmall(R.drawable.thumbs_down, "Dislike"){ /* dislike */ }
                    ActionChipSmall(R.drawable.lists, "Save"){ /* save */ }
                }
            }

            // Related
            RelatedVideos(
                currentId = video.id,
                navController = navController,
                currentUserEmail = currentUserEmail
            )
        }
    }
}

@Composable
private fun ActionChipSmall(
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(12.dp) // small icon
            )
        },
        modifier = Modifier.graphicsLayer(scaleX = 0.3f, scaleY = 0.3f) // 30% visual scale
    )
}


@Composable
private fun RelatedVideos(
    currentId: String,
    navController: NavController,
    currentUserEmail: String
) {
    val others = remember { VideoRepository.getRNGVid(10)}
    Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Text("Related", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        others.forEach { v ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("watch/${v.id}/$currentUserEmail") }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                GlideImage(
                    imageModel = { VideoUser.getThumbnail(v.id) },
                    modifier = Modifier
                        .width(150.dp)
                        .height(84.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(v.title, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        v.channel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

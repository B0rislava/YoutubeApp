package com.example.youtubeapp.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.example.youtubeapp.R
import com.example.youtubeapp.components.MainBottomBar
import com.example.youtubeapp.model.VideoItem
import com.example.youtubeapp.model.VideoRepository
import com.example.youtubeapp.model.VideoUser
import com.example.youtubeapp.navigation.Screen
import com.skydoves.landscapist.glide.GlideImage

@SuppressLint("ContextCastToActivity")
@Composable
fun VideoWatchScreen(
    video: VideoItem,
    navController: NavController,
    currentUserEmail: String
) {
    val scroll = rememberScrollState()
    val activity = LocalContext.current as Activity
    var isFullscreen by rememberSaveable { mutableStateOf(false) }

    // Apply/remove fullscreen side effects
    LaunchedEffect(isFullscreen) {
        if (isFullscreen) enterFullscreen(activity) else exitFullscreen(activity)
    }
    DisposableEffect(Unit) {
        onDispose { exitFullscreen(activity) }
    }
    BackHandler(enabled = isFullscreen) { isFullscreen = false }

    Scaffold(
        bottomBar = {
            if (!isFullscreen) {
                MainBottomBar(currentScreen = "Home") { screen ->
                    val tab = when (screen) {
                        "Home" -> 0
                        "Subscriptions" -> 1
                        "Profile" -> 2
                        else -> 0
                    }
                    navController.navigate("${Screen.Home.route}/$currentUserEmail?tab=$tab") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isFullscreen) {
            // Fullscreen player
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                VideoUser.Player(
                    videoId = video.id,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { isFullscreen = false },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.fullscrean_small),
                        contentDescription = "Exit fullscreen"
                    )
                }
            }
        } else {
            // Normal watch page
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scroll)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    VideoUser.Player(
                        videoId = video.id,
                        modifier = Modifier.matchParentSize()
                    )
                    IconButton(
                        onClick = { isFullscreen = true },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.fullscrean_big),
                            contentDescription = "Fullscreen"
                        )
                    }
                }

                // Title + meta + your action chips
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ActionChipSmall(R.drawable.thumbs_up,   "Like")   { /* like */ }
                        ActionChipSmall(R.drawable.thumbs_down, "Dislike") { /* dislike */ }
                    }
                }

                RelatedVideos(
                    currentId = video.id,
                    navController = navController,
                    currentUserEmail = currentUserEmail
                )
            }
        }
    }
}

private fun enterFullscreen(activity: Activity) {
    // Rotate to landscape and hide system bars
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    val window = activity.window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    controller.hide(WindowInsetsCompat.Type.systemBars())
}

private fun exitFullscreen(activity: Activity) {
    // Restore orientation and system bars
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    val window = activity.window
    WindowCompat.setDecorFitsSystemWindows(window, true)
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.show(WindowInsetsCompat.Type.systemBars())
}

// Your existing helpers (kept as-is)
@Composable
private fun ActionChipSmall(icon: Int, label: String, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(12.dp)
            )
        }
    )
}

@Composable
private fun RelatedVideos(
    currentId: String,
    navController: NavController,
    currentUserEmail: String
) {
    val others = remember { VideoRepository.getRNGVid(10) }
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

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.example.youtubeapp.R
import com.example.youtubeapp.components.MainBottomBar
import com.example.youtubeapp.data.local.DatabaseProvider
import com.example.youtubeapp.model.VideoItem
import com.example.youtubeapp.model.VideoRepository
import com.example.youtubeapp.model.VideoUser
import com.example.youtubeapp.navigation.Screen
import com.example.youtubeapp.repository.SimpleListsRepository
import com.example.youtubeapp.repository.UserRepository
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

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

    // --- Build repos here directly ---
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val userRepo = remember { UserRepository(db.userDao()) }
    val listsRepo = remember {
        SimpleListsRepository(
            likes = db.likesDao(),
            dislikes = db.dislikesDao(),
            subs = db.subscriptionsDao(),
            history = db.historyDao()
        )
    }

    // --- State for this screen ---
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    var isSubscribed by remember { mutableStateOf(false) }
    var userId by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()

    // Load user + initial states once
    LaunchedEffect(Unit) {
        val user = userRepo.getUserByEmail(currentUserEmail)
        userId = user?.uid
        userId?.let { uid ->
            val liked = listsRepo.getLiked(uid)
            val disliked = listsRepo.getDisliked(uid)
            val subs = listsRepo.getSubscriptions(uid)
            isLiked = liked.contains(video.id)
            isDisliked = disliked.contains(video.id)
            isSubscribed = subs.contains(video.channel)
            // log watch in history
            listsRepo.addToHistory(uid, video.id)
        }
    }

    // Apply/remove fullscreen side effects
    LaunchedEffect(isFullscreen) {
        if (isFullscreen) enterFullscreen(activity) else exitFullscreen(activity)
    }
    DisposableEffect(Unit) { onDispose { exitFullscreen(activity) } }
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

                        // --- Red Subscribe button (YouTube style) ---
                        val ytRed = Color(0xFFE62117)
                        val subColors = if (!isSubscribed) {
                            ButtonDefaults.buttonColors(
                                containerColor = ytRed,
                                contentColor = Color.White
                            )
                        } else {
                            // Neutral look when already subscribed
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = {
                                userId?.let { uid ->
                                    scope.launch {
                                        if (isSubscribed) {
                                            listsRepo.unsubscribe(uid, video.channel)
                                            isSubscribed = false
                                        } else {
                                            listsRepo.subscribe(uid, video.channel)
                                            isSubscribed = true
                                        }
                                    }
                                }
                            },
                            colors = subColors,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(if (isSubscribed) "Subscribed" else "Subscribe")
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // --- Filled chips when selected (Like/Dislike) ---
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // LIKE chip
                        FilterChip(
                            selected = isLiked,
                            onClick = {
                                userId?.let { uid ->
                                    scope.launch {
                                        if (isLiked) {
                                            listsRepo.removeLike(uid, video.id)
                                            isLiked = false
                                        } else {
                                            listsRepo.like(uid, video.id)
                                            isLiked = true
                                            isDisliked = false
                                        }
                                    }
                                }
                            },
                            label = { Text(if (isLiked) "Liked" else "Like") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.thumbs_up),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4CAF50),
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )

                        // DISLIKE chip
                        FilterChip(
                            selected = isDisliked,
                            onClick = {
                                userId?.let { uid ->
                                    scope.launch {
                                        if (isDisliked) {
                                            listsRepo.removeDislike(uid, video.id)
                                            isDisliked = false
                                        } else {
                                            listsRepo.dislike(uid, video.id)
                                            isDisliked = true
                                            isLiked = false
                                        }
                                    }
                                }
                            },
                            label = { Text(if (isDisliked) "Disliked" else "Dislike") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.thumbs_down),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE62117),
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )
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
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    val window = activity.window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    controller.hide(WindowInsetsCompat.Type.systemBars())
}

private fun exitFullscreen(activity: Activity) {
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    val window = activity.window
    WindowCompat.setDecorFitsSystemWindows(window, true)
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.show(WindowInsetsCompat.Type.systemBars())
}

/* Removed old AssistChip; we now use FilterChip above for selectable/filled look */

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

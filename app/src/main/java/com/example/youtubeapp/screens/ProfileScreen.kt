package com.example.youtubeapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.youtubeapp.R
import com.example.youtubeapp.data.local.DatabaseProvider
import com.example.youtubeapp.model.VideoItem
import com.example.youtubeapp.model.VideoRepository
import com.example.youtubeapp.model.VideoUser
import com.example.youtubeapp.navigation.Screen
import com.example.youtubeapp.repository.SimpleListsRepository
import com.example.youtubeapp.repository.UserRepository
import com.example.youtubeapp.viewmodel.ProfileViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {
    val scroll = rememberScrollState()
    val context = androidx.compose.ui.platform.LocalContext.current
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

    val email = viewModel.userEmail.value
    val name = viewModel.name.value

    var userId by remember { mutableStateOf<Int?>(null) }
    var likedIds by remember { mutableStateOf<List<String>>(emptyList()) }
    var dislikedIds by remember { mutableStateOf<List<String>>(emptyList()) }
    var historyIds by remember { mutableStateOf<List<String>>(emptyList()) }

    // dropdown expand states
    var likedOpen by remember { mutableStateOf(false) }
    var dislikedOpen by remember { mutableStateOf(false) }
    var historyOpen by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Load user + lists
    LaunchedEffect(email) {
        val user = userRepo.getUserByEmail(email)
        userId = user?.uid
        userId?.let { uid ->
            likedIds = listsRepo.getLiked(uid)
            dislikedIds = listsRepo.getDisliked(uid)
            historyIds = listsRepo.getHistory(uid, limit = 200)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header: Name + Email
        Text(
            text = "Welcome, $name!",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Email: $email",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Divider()

        // Liked videos
        ExpandableSection(
            title = "Liked videos",
            count = likedIds.size,
            Icon = painterResource(R.drawable.thumbs_up),
            expanded = likedOpen,
            onToggle = { likedOpen = !likedOpen }
        ) {
            VideoIdList(ids = likedIds, navController = navController, currentUserEmail = email)
        }

        // Disliked videos
        ExpandableSection(
            title = "Disliked videos",
            count = dislikedIds.size,
            Icon = painterResource(R.drawable.thumbs_down),
            expanded = dislikedOpen,
            onToggle = { dislikedOpen = !dislikedOpen }
        ) {
            VideoIdList(ids = dislikedIds, navController = navController, currentUserEmail = email)
        }

        // History
        ExpandableSection(
            title = "Watch history",
            count = historyIds.size,
            Icon = painterResource(R.drawable.visible),
            expanded = historyOpen,
            onToggle = { historyOpen = !historyOpen }
        ) {
            VideoIdList(ids = historyIds, navController = navController, currentUserEmail = email)
        }

        Spacer(Modifier.height(8.dp))

        // Sign out
        Button(
            onClick = {
                navController.navigate(Screen.SignIn.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE62117),
                contentColor = Color.White
            ),
        ) {
            Text("Sign out")
        }

        Spacer(Modifier.height(8.dp))
    }
}


@Composable
private fun ExpandableSection(
    title: String,
    count: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    Icon: Painter? = null,   // renamed for clarity
    content: @Composable () -> Unit
) {
    Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.medium) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title + count on the left
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$count item${if (count == 1) "" else "s"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (Icon != null) {
                    Icon(
                        painter = Icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(start = 8.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    content()
                }
            }
        }
    }
}



@Composable
private fun VideoIdList(
    ids: List<String>,
    navController: NavController,
    currentUserEmail: String
) {
    if (ids.isEmpty()) {
        Text(
            "Nothing here yet.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ids.forEach { id ->
            val video: VideoItem? = remember(id) { VideoRepository.videos.find { it.id == id } }
            if (video != null) {
                VideoRow(video, navController, currentUserEmail)
            } else {
                // Fallback if the ID isn't in your in-memory list
                Text(
                    text = "Video $id",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Divider()
        }
    }
}

@Composable
private fun VideoRow(
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

package com.example.youtubeapp.screens

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.youtubeapp.R
import com.example.youtubeapp.VideoItem
import com.example.youtubeapp.VideoRepository
import com.example.youtubeapp.VideoUser
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {

    val items = listOf("Home", "Subscriptions", "Profile")
    val icons = listOf(
        painterResource(id = R.drawable.home),
        painterResource(id = R.drawable.lists),
        painterResource(id = R.drawable.profile)
    )
    val sampleVideos = VideoRepository.getRNGVid(10)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.youtube),
                            contentDescription = "YouTube logo",
                            modifier = Modifier.size(36.dp)
                        )
                        Text(text = "AliExpress YouTube")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.search),
                            contentDescription = "Search",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                VideoList(videos = sampleVideos, size = 200)
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon( icons[0], contentDescription = items[0]) },
                    label = { Text(items[0]) },
                    modifier = Modifier.size(24.dp),
                    selected = true,
                    onClick = {  }
                )
                NavigationBarItem(
                    icon = { Icon( icons[1], contentDescription = items[1]) },
                    label = { Text(items[1]) },
                    modifier = Modifier.size(24.dp),
                    selected = false,
                    onClick = {  }
                )
                NavigationBarItem(
                    icon = { Icon( icons[2], contentDescription = items[2]) },
                    label = { Text(items[2]) },
                    modifier = Modifier.size(24.dp),
                    selected = false,
                    onClick = {  }
                )
            }
        }
    )
}

@Composable
fun VideoList(videos: List<VideoItem>, size: Int) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(videos) { video ->
            var playVideo by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { playVideo = !playVideo },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    if (playVideo) {
                        VideoUser.Player(
                            videoId = video.id,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(size.dp)
                        )
                    } else {
                        GlideImage(
                            imageModel = { VideoUser.getThumbnail(video.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = video.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = video.channel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}









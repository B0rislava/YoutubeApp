    package com.example.youtubeapp.screens

    import android.annotation.SuppressLint
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
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.material3.TopAppBar
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableIntStateOf
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import com.example.youtubeapp.R
    import com.example.youtubeapp.model.VideoItem
    import com.example.youtubeapp.model.VideoRepository
    import com.example.youtubeapp.model.VideoUser
    import com.example.youtubeapp.components.MainBottomBar
    import com.example.youtubeapp.data.local.DatabaseProvider
    import com.example.youtubeapp.repository.UserRepository
    import com.example.youtubeapp.viewmodel.ProfileViewModel
    import com.example.youtubeapp.viewmodel.factory.ProfileViewModelFactory
    import com.skydoves.landscapist.glide.GlideImage

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun HomeScreen(
        currentUserEmail: String,
        navController: NavController
    ) {

        val context = LocalContext.current
        val userRepository = UserRepository(DatabaseProvider.getDatabase(context).userDao())

        val profileViewModel: ProfileViewModel = viewModel(
            factory = ProfileViewModelFactory(currentUserEmail, userRepository)
        )

        var selectedTab by remember { mutableIntStateOf(0) }

        val navigateToTab: (String) -> Unit = { screen ->
            selectedTab = when (screen) {
                "Home" -> 0
                "Subscriptions" -> 1
                "Profile" -> 2
                else -> 0
            }
        }

        var isSearching by remember { mutableStateOf(false) }
        var searchQuery by remember { mutableStateOf("") }

        val allVideos = VideoRepository.videos

        val displayedVideos = if (searchQuery.isNotBlank()) {
            allVideos.filter { video ->
                video.title.contains(searchQuery, ignoreCase = true) ||
                        video.channel.contains(searchQuery, ignoreCase = true)
            }
        } else {
            VideoRepository.getRNGVid(10)
        }

        Scaffold(
            topBar = {
                if (selectedTab == 0) {
                    TopAppBar(
                        title = {
                            if (isSearching) {
                                TextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = { Text("Search videos...") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
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
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                if (isSearching) {
                                    searchQuery = ""
                                }
                                isSearching = !isSearching
                            }) {
                                Icon(
                                    painter = painterResource(
                                        if (isSearching) R.drawable.x else R.drawable.search
                                    ),
                                    contentDescription = "Search",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    )
                }
            },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (selectedTab) {
                        0 -> VideoList(videos = displayedVideos, size = 200)
                        1 -> SubscriptionsScreen(currentUserEmail, navController)
                        2 -> ProfileScreen(profileViewModel, navController)
                    }
                }
            },
            bottomBar = {
                MainBottomBar(
                    currentScreen = when (selectedTab) {
                        0 -> "Home"
                        1 -> "Subscriptions"
                        else -> "Profile"
                    }
                ) { screen ->
                    navigateToTab(screen)
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

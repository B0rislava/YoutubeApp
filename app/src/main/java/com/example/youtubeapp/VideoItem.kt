package com.example.youtubeapp

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

data class VideoItem(
    val id: String,
    val title: String,
    val channel: String) {
}

object VideoUser {

    @SuppressLint("ContextCastToActivity")
    @Composable
    fun Player(videoId: String, modifier: Modifier = Modifier) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                YouTubePlayerView(context).apply {
                    // Attach lifecycle owner safely
                    (parent as? ViewGroup)?.findViewTreeLifecycleOwner()?.lifecycle?.addObserver(this)

                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(player: YouTubePlayer) {
                            player.loadVideo(videoId, 0f)
                        }
                    })
                }
            }
        )
    }

    fun getThumbnail(videoId: String): String {
        return "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
    }
}

object VideoRepository {
    fun getRNGVid(count: Int): List<VideoItem> {
        return videos.shuffled().take(count.coerceAtMost(videos.size))
    }

    val videos = listOf(
        VideoItem("dQw4w9WgXcQ", "Never Gonna Give You Up", "Rick Astley"),
        VideoItem("3JZ_D3ELwOQ", "Charlie bit my finger!", "HDCYT"),
        VideoItem("9bZkp7q19f0", "Gangnam Style", "PSY"),
        VideoItem("XqZsoesa55w", "Baby Shark Dance", "Pinkfong"),
        VideoItem("kJQP7kiw5Fk", "Despacito", "Luis Fonsi ft. Daddy Yankee"),
        VideoItem("RgKAFK5djSk", "See You Again", "Wiz Khalifa ft. Charlie Puth"),
        VideoItem("JGwWNGJdvx8", "Shape of You", "Ed Sheeran"),
        VideoItem("OPf0YbXqDm0", "Uptown Funk", "Mark Ronson ft. Bruno Mars"),
        VideoItem("e_04ZrNroTo", "Wheels on the Bus", "CoComelon"),
        VideoItem("WRVsOCh907o", "Bath Song", "CoComelon"),
        VideoItem("D1LDPmYoYm4", "Johny Johny Yes Papa", "CoComelon"),
        VideoItem("2n4enj6YMnw", "Phonics Song with Two Words", "ChuChu TV"),
        VideoItem("_nAu9D-8srA", "Learning Colors – Colorful Eggs on a Farm", "[Channel Name]"),
        VideoItem("Y8d86tUxNf8", "Axel F", "Crazy Frog"),
        VideoItem("FzG4uDgje3M", "Dame Tu Cosita", "El Chombo feat. Cutty Ranks"),
        VideoItem("XYczqkXxaXM", "Shree Hanuman Chalisa", "T-Series Bhakti Sagar"),
        VideoItem("KYniUCGPGLs", "Masha and the Bear – Recipe For Disaster", "Masha and the Bear"),
        VideoItem("MR5XSOdjKMA", "Baa Baa Black Sheep", "CoComelon"),
        VideoItem("3bLfzgZ-wO8", "Lakdi Ki Kathi", "JingleToons"),
        VideoItem("pRpeEdMmmQ0", "Waka Waka (This Time for Africa)", "Shakira ft. Freshlyground"),
        VideoItem("09R8_2nJtjg", "Sugar", "Maroon 5"),
        VideoItem("hT_nvWreIhg", "Counting Stars", "OneRepublic"),
        VideoItem("2Vv-BfVoq4g", "Perfect", "Ed Sheeran"),
        VideoItem("qGnzExdoSp4", "Shree Hanuman Chalisa (Alt)", "T-Series Bhakti Sagar"),
        VideoItem("QgRtJt88mGE", "Lakdi Ki Kathi (Alt)", "JingleToons"),
        VideoItem("CevxZvSJLk8", "Roar", "Katy Perry"),
        VideoItem("0KSOMA3QBU0", "Dark Horse ft. Juicy J", "Katy Perry"),
        VideoItem("YQHsXMglC9A", "Hello", "Adele"),
        VideoItem("e-ORhEE9VVg", "Blank Space", "Taylor Swift"),
        VideoItem("nfWlot6h_JM", "Shake It Off", "Taylor Swift"),
        VideoItem("7wtfhZwyrcc", "Believer", "Imagine Dragons"),
        VideoItem("fKopy74weus", "Thunder", "Imagine Dragons"),
        VideoItem("0zGcUoRlhmw", "Closer", "The Chainsmokers ft. Halsey"),
        VideoItem("YqeW9_5kURI", "Lean On (feat. MØ)", "Major Lazer & DJ Snake"),
        VideoItem("RBumgq5yVrA", "Let Her Go", "Passenger"),
        VideoItem("60ItHLz5WEA", "Faded", "Alan Walker"),
        VideoItem("kffacxfA7G4", "Baby", "Justin Bieber ft. Ludacris"),
        VideoItem("DyDfgMOUjCI", "bad guy", "Billie Eilish"),
        VideoItem("BQ0mxQXmLsk", "Havana (feat. Young Thug)", "Camila Cabello"),
        VideoItem("k2qgadSvNyU", "New Rules", "Dua Lipa"),
        VideoItem("gdZLi9oWNZg", "Dynamite", "BTS"),
        VideoItem("ixkoVwKQaJg", "Taki Taki", "DJ Snake ft. Selena Gomez, Ozuna, Cardi B"),
        VideoItem("34Na4j8AVgA", "Starboy (feat. Daft Punk)", "The Weeknd"),
        VideoItem("wnJ6LuUFpMo", "Mi Gente", "J Balvin, Willy William"),
        VideoItem("Pkh8UtuejGw", "Señorita", "Shawn Mendes & Camila Cabello"),
        VideoItem("ioNng23DkIM", "How You Like That", "BLACKPINK"),
        VideoItem("qrO4YZeyl0I", "Bad Romance", "Lady Gaga")
    )
}


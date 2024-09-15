package ir.ha.cofeeplayer

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ha.cofeeplayer.common.AnimatedCircleProgressBar
import ir.ha.cofeeplayer.common.ExoPlayerHelper
import ir.ha.cofeeplayer.screens.MusicPlayerScreen
import ir.ha.cofeeplayer.screens.SongCover
import ir.ha.cofeeplayer.ui.theme.CofeePlayerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CofeePlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimatedCircleProgressBar(
                        progress = 24f,
                        modifier = Modifier.padding(innerPadding),
                        size = 200.dp,
                        strokeWidth = 52.dp,
                        progressColor = Color.Green,
                        backgroundColor = Color.White,
                        animDuration = 5000,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    val context = LocalContext.current // Get context in the composable

    val exoPlayerHelper = remember { ExoPlayerHelper(context) }

    val songTitle by remember { mutableStateOf("Ghroor") }
    val artistName by remember { mutableStateOf("Shadmehr Aghili") }
    val albumName by remember { mutableStateOf("Tasvir") }

    val songUri  : Uri ?= null

    val songCover by remember { mutableStateOf<SongCover>(SongCover.Drawable(R.drawable.cover)) }

    val songDurations by remember { mutableIntStateOf(1) }

    var isPlaying by remember { mutableStateOf(false) }
    var isRepeatOn by remember { mutableStateOf(false) }
    var isShuffleOn by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var isMuteOn by remember { mutableStateOf(false) }

    MusicPlayerScreen(
        exoPlayerHelper = exoPlayerHelper,
        songUri = songUri,
        songTitle = songTitle,
        artistName = artistName,
        albumName = albumName,
        songCover = songCover ,
        totalDuration = songDurations,
        isPlaying = isPlaying,
        isRepeatOn = isRepeatOn,
        isShuffleOn = isShuffleOn,
        isFavorite = isFavorite,
        isMuteOn = isMuteOn ,
        onPlayPauseClicked = {
            isPlaying = isPlaying.not()
        },
        onNextClicked = {
            Toast.makeText(context,"onNextClicked",Toast.LENGTH_SHORT).show()
        },
        onPreviousClicked = {
            Toast.makeText(context,"onPreviousClicked",Toast.LENGTH_SHORT).show()
        },
        onRepeatClicked = {
            isRepeatOn = isRepeatOn.not()
        },
        onShuffleClicked = {
            isShuffleOn = isShuffleOn.not()
        },
        onFavoriteClicked = {
            isFavorite = isFavorite.not()
        },
        onBackClicked = {
            Toast.makeText(context,"onBackClicked",Toast.LENGTH_SHORT).show()
        },
        onMoreClicked = {
            Toast.makeText(context,"onMoreClicked",Toast.LENGTH_SHORT).show()
        },
        onMuteClicked = {
            isMuteOn = isMuteOn.not()
        },
    )
}
package ir.ha.cofeeplayer.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ir.ha.cofeeplayer.common.ExoPlayerHelper
import ir.ha.cofeeplayer.data.database.SongEntity
import ir.ha.cofeeplayer.screens.CoroutineTimer
import ir.ha.cofeeplayer.screens.MainScreen
import ir.ha.cofeeplayer.ui.theme.CofeePlayerTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : MainActivityVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CofeePlayerTheme {

                Surface {
                    SideEffect {
                        viewModel.fetchSongs()
                    }

                    val loading by viewModel.loading.collectAsState()
                    val error by viewModel.error.collectAsState()
                    val data by viewModel.data.collectAsState()


                    when {
                        loading -> {
                            // Show loading indicator
                            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                        }

                        error != null -> {
                            // Show error message
                            Text(
                                text = "Error: $error",
                                modifier = Modifier.fillMaxSize(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        data.isNullOrEmpty().not() -> {
                            // Show the list of users when data is available
                            Surface {

                                val context = LocalContext.current
                                val exoPlayerHelper = remember { ExoPlayerHelper(context) }
                                var isExpanded by remember { mutableStateOf(false) }
                                var isPlaying by remember { mutableStateOf(false) }
                                var isRepeatOn by remember { mutableStateOf(false) }
                                var isShuffleOn by remember { mutableStateOf(false) }
                                var isFavorite by remember { mutableStateOf(false) }
                                var isMuteOn by remember { mutableStateOf(false) }

                                val songs by rememberSaveable{ mutableStateOf(data?: arrayListOf()) }
                                var songPosition by remember { mutableIntStateOf(if(songs.isEmpty()) -1 else 0) }
                                var currentLeftTime by remember { mutableIntStateOf(0) }


                                val timer = remember { CoroutineTimer(
                                    totalSeconds = songs[songPosition].songDuration,
                                    onTick = {
                                        currentLeftTime = it
                                    },
                                    onFinished = {
                                        currentLeftTime = 0
                                        isPlaying = false
                                    })
                                }


                                MainScreen(
                                    selectedSong = Pair(songs,songPosition),
                                    isExpanded = isExpanded,
                                    isPlaying = isPlaying,
                                    isRepeatOn = isRepeatOn,
                                    isShuffleOn = isShuffleOn,
                                    isFavorite = isFavorite,
                                    isMuteOn = isMuteOn,
                                    currentLeftTime = currentLeftTime,
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
                                        isExpanded = isExpanded.not()
                                    },
                                    onMoreClicked = {
                                        Toast.makeText(context, "onMoreClicked", Toast.LENGTH_SHORT).show()
                                    },
                                    onMuteClicked = {
                                        isMuteOn = isMuteOn.not()
                                    },
                                    onSoundBarClick = {
                                        if(songPosition != -1){
                                            isExpanded = isExpanded.not()
                                        }
                                    },
                                    onNextClicked = {
                                        Toast.makeText(context, "onNextClicked", Toast.LENGTH_SHORT).show()
                                    },
                                    onPreviousClicked = {
                                        Toast.makeText(context, "onPreviousClicked", Toast.LENGTH_SHORT).show()
                                    },
                                    onSongClick = { pos ->
                                        if (songs.size -1 >= pos) {
                                            if (isPlaying.not()) isPlaying = true
                                            songPosition = pos
                                            playSong(exoPlayerHelper, songs, songPosition)
                                            currentLeftTime = 0
                                            timer.stop()
                                            timer.start(songs[songPosition].songDuration)
                                        }
                                    },
                                    onPlayPauseClicked = {
                                        if (isPlaying) {
                                            isPlaying = false
                                            exoPlayerHelper.pause()
                                            timer.pause()
                                        }
                                        else {
                                            isPlaying = true
                                            exoPlayerHelper.play()
                                            timer.resume()
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun playSong(
        exoPlayerHelper: ExoPlayerHelper,
        songs: List<SongEntity>,
        songPosition: Int
    ) {
        exoPlayerHelper.initializePlayer(songs[songPosition].songUri)
        exoPlayerHelper.play()
    }
}



@Preview(showBackground = true)
@Composable
fun MainActivityPreviews() {

}
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
import androidx.compose.material3.Scaffold
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
import ir.ha.cofeeplayer.screens.MainScreen
import ir.ha.cofeeplayer.screens.Timer
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
                                val timer = remember { Timer(this.lifecycleScope) }
                                val exoPlayerHelper = remember { ExoPlayerHelper(context) }
                                var isExpanded by rememberSaveable { mutableStateOf(false) }
                                var isPlaying by rememberSaveable { mutableStateOf(false) }
                                var isRepeatOn by rememberSaveable { mutableStateOf(false) }
                                var isShuffleOn by rememberSaveable { mutableStateOf(false) }
                                var isFavorite by rememberSaveable { mutableStateOf(false) }
                                var isMuteOn by rememberSaveable { mutableStateOf(false) }

                                val songs by remember{ mutableStateOf(data?: arrayListOf()) }
                                var songPosition by remember { mutableIntStateOf(if(songs.isEmpty()) -1 else 0) }
                                var currentLeftTime by remember { mutableIntStateOf(0) }

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
                                            isPlaying = true
                                            songPosition = pos
                                            exoPlayerHelper.initializePlayer(songs[pos].songUri)
                                            exoPlayerHelper.play()
                                            timer.stopTimer()
                                            timer.startTimer(songs[pos].songDuration,
                                                onTick = { t ->
                                                    currentLeftTime = t
                                                },
                                                onComplete = {
                                                    isPlaying = false
                                                    exoPlayerHelper.pause()
                                                    currentLeftTime = 0
                                                }
                                            )
                                        }
                                    },
                                    onPlayPauseClicked = { play ->
                                        if (play) {
                                            exoPlayerHelper.pause()
                                            isPlaying = false
                                        }
                                        else {
                                            exoPlayerHelper.play()
                                            isPlaying = true
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
}



@Preview(showBackground = true)
@Composable
fun MainActivityPreviews() {

}
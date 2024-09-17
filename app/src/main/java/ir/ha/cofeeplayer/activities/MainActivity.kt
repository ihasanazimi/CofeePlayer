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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import ir.ha.cofeeplayer.common.ExoPlayerHelper
import ir.ha.cofeeplayer.data.database.SongEntity
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
                            var isExpanded by rememberSaveable { mutableStateOf(false) }
                            var isPlaying by rememberSaveable { mutableStateOf(false) }
                            var isRepeatOn by rememberSaveable { mutableStateOf(false) }
                            var isShuffleOn by rememberSaveable { mutableStateOf(false) }
                            var isFavorite by rememberSaveable { mutableStateOf(false) }
                            var isMuteOn by rememberSaveable { mutableStateOf(false) }
                            var songEntity by remember { mutableStateOf<SongEntity?>(null) }

                            MainScreen(
                                exoPlayerHelper = exoPlayerHelper,
                                songEntity = songEntity,
                                songs = data?: arrayListOf(),
                                isExpanded = isExpanded,
                                isPlaying = isPlaying,
                                isRepeatOn = isRepeatOn,
                                isShuffleOn = isShuffleOn,
                                isFavorite = isFavorite,
                                isMuteOn = isMuteOn,
                                onPlayPauseClicked = { play ->
                                    if (play) exoPlayerHelper.pause()
                                    else exoPlayerHelper.play()
                                    isPlaying = play
                                },
                                onNextClicked = {
                                    Toast.makeText(context, "onNextClicked", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                onPreviousClicked = {
                                    Toast.makeText(context, "onPreviousClicked", Toast.LENGTH_SHORT)
                                        .show()
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
                                    isExpanded = isExpanded.not()
                                },
                                onMoreClicked = {
                                    Toast.makeText(context, "onMoreClicked", Toast.LENGTH_SHORT).show()
                                },
                                onMuteClicked = {
                                    isMuteOn = isMuteOn.not()
                                },
                                onSoundBarClick = {
                                    isExpanded = isExpanded.not()
                                },
                                onSongClick = { song ->
                                    songEntity = song
                                    if (songEntity != null){
                                        isPlaying = true
                                        exoPlayerHelper.initializePlayer(songEntity!!.songUrl)
                                        exoPlayerHelper.play()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun Previewsss() {

}
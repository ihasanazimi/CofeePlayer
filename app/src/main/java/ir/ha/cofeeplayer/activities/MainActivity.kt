package ir.ha.cofeeplayer.activities

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import ir.ha.cofeeplayer.R
import ir.ha.cofeeplayer.activities.MainActivity.Companion.ACTION_NEXT
import ir.ha.cofeeplayer.activities.MainActivity.Companion.ACTION_PLAY_PAUSE
import ir.ha.cofeeplayer.activities.MainActivity.Companion.ACTION_PREVIOUS
import ir.ha.cofeeplayer.common.ExoPlayerHelper
import ir.ha.cofeeplayer.data.database.SongEntity
import ir.ha.cofeeplayer.screens.CoroutineTimer
import ir.ha.cofeeplayer.screens.MainScreen
import ir.ha.cofeeplayer.ui.theme.CofeePlayerTheme
import kotlinx.coroutines.delay
import okhttp3.internal.notify


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : MainActivityVM by viewModels()


    companion object{
        const val ACTION_PLAY_PAUSE = "com.example.app.ACTION_PLAY_PAUSE"
        const val ACTION_NEXT = "com.example.app.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.example.app.ACTION_PREVIOUS"
    }

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

                                val exoPlayer = remember { exoPlayerHelper.exoObject() }
                                var sliderPosition by remember { mutableFloatStateOf(0f) }
                                var isUserSliding by remember { mutableStateOf(false) }
                                val duration = remember { exoPlayerHelper.exoObject()?.duration?.coerceAtLeast(songs[songPosition].songDuration.toLong()) } // Avoid division by zero
                                val currentPosition = exoPlayerHelper.exoObject()?.currentPosition



                                // Observe ExoPlayer's progress in a coroutine and update the slider
                                LaunchedEffect(exoPlayerHelper.exoObject()) {
                                    while (true) {
                                        if (!isUserSliding) {
                                            exoPlayer?.let { player ->
                                                val currentPos = player.currentPosition
                                                val dur = player.duration.coerceAtLeast(1L) // Ensure duration is at least 1
                                                sliderPosition = currentPos / dur.toFloat()
                                            }
                                        }
                                        delay(1000L) // Update every second
                                    }
                                }


                                MainScreen(
                                    selectedSong = Pair(songs,songPosition),
                                    isExpanded = isExpanded,
                                    isPlaying = isPlaying,
                                    isRepeatOn = isRepeatOn,
                                    isShuffleOn = isShuffleOn,
                                    isFavorite = isFavorite,
                                    isMuteOn = isMuteOn,
                                    currentLeftTime = sliderPosition,
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
                                            playSong(context , exoPlayerHelper, songs, songPosition)
                                            sliderPosition = 0f
                                            /*timer.stop()
                                            timer.start(songs[songPosition].songDuration)*/
                                        }
                                    },
                                    onPlayPauseClicked = {
                                        if (isPlaying) {
                                            isPlaying = false
                                            exoPlayerHelper.pause()
//                                            timer.pause()
                                        }
                                        else {
                                            isPlaying = true
                                            exoPlayerHelper.play()
//                                            timer.resume()
                                        }
                                    },
                                    sliderPosition = { p ->
                                        sliderPosition = p
                                    },
                                    isUserSliding = { isSliding ->
                                       isUserSliding = isSliding
                                        if (isSliding.not()) {
                                            exoPlayer?.seekTo(sliderPosition.toLong())
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

    private fun playSong(
        context: Context,
        exoPlayerHelper: ExoPlayerHelper,
        songs: List<SongEntity>,
        songPosition: Int
    ) {
        exoPlayerHelper.initializePlayer(songs[songPosition].songUri)
        exoPlayerHelper.play()
    }


    private fun createNotification(context : Context , exoPlayerHelper: ExoPlayerHelper) : NotificationCompat.Builder {
        val mediaSession = MediaSessionCompat(context, "MediaSessionTag")
        // Set session to active
        mediaSession.isActive = true
        return NotificationCompat.Builder(context, "Music_Channel_ID")
            .setContentTitle("Now Playing")
            .setContentText("Track Title")
            .setSmallIcon(R.drawable.cover)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(R.drawable.previews, "Previous", getActionIntent(context,ACTION_PREVIOUS))
            .addAction(R.drawable.play, "Play/Pause", getActionIntent(context,ACTION_PLAY_PAUSE))
            .addAction(R.drawable.next, "Next", getActionIntent(context,ACTION_NEXT))
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(1) // Play/Pause button index
            )
            .setProgress(exoPlayerHelper.duration()?.toInt()?:0, exoPlayerHelper.exoObject()?.currentPosition?.toInt()?:0, false) // Update duration
            .setOngoing(true) // Keep the notification active
    }




    fun notifyNotification(context: Context, exoPlayerHelper: ExoPlayerHelper){

        val notificationID = 5008
        val notificationManager = createNotification(context,exoPlayerHelper)

        exoPlayerHelper.exoObject()?.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                // Update the notification with new progress
                notificationManager.setProgress(
                    exoPlayerHelper.exoObject()?.duration?.toInt() ?: 0, exoPlayerHelper?.exoObject()?.currentPosition?.toInt() ?: 0, false
                )
                notificationManager.notify()
            }
        })

    }




}



private fun getActionIntent(context: Context,action: String): PendingIntent {
    val intent = Intent(context, MyBroadcastReceiver::class.java).apply {
        this.action = action
    }
    return PendingIntent.getBroadcast(
        context,
        action.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}



class MyBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context : Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_PLAY_PAUSE -> {
                // Toggle play/pause
//                if (player.isPlaying) player.pause() else player.play()
            }
            ACTION_NEXT -> {
                // Play next track
            }
            ACTION_PREVIOUS -> {
                // Play previous track
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MainActivityPreviews() {

}
package ir.ha.cofeeplayer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.ha.cofeeplayer.data.database.SongEntity


val TAG = "TAG"

@Composable
fun MainScreen(
    isExpanded: Boolean = true,
    selectedSong : Pair<List<SongEntity>,Int>,
    isPlaying: Boolean,
    isMuteOn: Boolean,
    isRepeatOn: Boolean,
    isShuffleOn: Boolean,
    isFavorite: Boolean,
    currentLeftTime: Float,
    onPlayPauseClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
    onPreviousClicked: () -> Unit = {},
    onRepeatClicked: () -> Unit = {},
    onShuffleClicked: () -> Unit = {},
    onFavoriteClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onMoreClicked: () -> Unit = {},
    onMuteClicked: () -> Unit = {},
    onSoundBarClick: () -> Unit = {},
    onSongClick: (Int) -> Unit = { _ -> },
    sliderPosition : (Float) -> Unit ,
    isUserSliding  : (Boolean) -> Unit,
) {

    val context = LocalContext.current // Get context in the composable

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable { /* تریگر کردن برای باز و بسته شدن */ },
    ) {


        Box(
            modifier = Modifier
                .weight(0.9f)
                .padding(8.dp)
        ) {
            if (isExpanded) {

                PlayerScreen(
                    context = context,
                    songEntity = selectedSong.first[selectedSong.second],
                    currentLeftTime = currentLeftTime,
                    isPlaying = isPlaying,
                    isMuteOn = isMuteOn,
                    isFavorite = isFavorite,
                    isShuffleOn = isShuffleOn,
                    isRepeatOn = isRepeatOn,
                    onMuteClicked = {
                        onMuteClicked.invoke()
                    },
                    onMoreClicked = {
                        onMoreClicked.invoke()
                    },
                    onBackClicked = {
                        onBackClicked.invoke()
                    },
                    onFavoriteClicked = {
                        onFavoriteClicked.invoke()
                    },
                    onShuffleClicked = {
                        onShuffleClicked.invoke()
                    },
                    onRepeatClicked = {
                        onRepeatClicked.invoke()
                    },
                    onPreviousClicked = {
                        onPreviousClicked.invoke()
                    },
                    onNextClicked = {
                        onNextClicked.invoke()
                    },
                    onPlayPauseClicked = {
                        onPlayPauseClicked.invoke()
                    },
                    sliderPosition = { p ->
                        sliderPosition.invoke(p)
                    },
                    isUserSliding = { isSliding ->
                        isUserSliding.invoke(isSliding)
                    }
                )

            } else {

                SongListScreen(
                    selectedSong = selectedSong,
                    onSongClick = { songPosition ->
                    onSongClick.invoke(songPosition)
                })

            }
        }


        Box(
            modifier = Modifier
                .padding(8.dp)
                .weight(if (isExpanded.not()) 0.109f else 0.01f)
        ) {

            SoundBar(
                isPlaying = isPlaying,
                selectedSong = selectedSong,
                onSoundBarClick = {
                    onSoundBarClick.invoke()
                },
                onPlayPauseClicked = {
                    onPlayPauseClicked.invoke()
                }
            )

        }

    }
}


@Preview(showBackground = true)
@Composable
fun Previews() {
}
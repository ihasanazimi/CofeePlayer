package ir.ha.cofeeplayer.ui.theme

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import ir.ha.cofeeplayer.LinearProgressBar
import ir.ha.cofeeplayer.R

@Composable
fun MusicPlayerScreen(
    songTitle: String = "Unknown",
    artistName: String = "Unknown",
    albumName: String = "Unknown",
    songCover: SongCover = SongCover.Drawable(R.drawable.cover),
    isPlaying: Boolean = false,
    isRepeatOn: Boolean = false,
    isShuffleOn: Boolean = false,
    isFavorite: Boolean = false,
    totalDuration: Int = 30, // مدت زمان کل آهنگ به ثانیه
    onPlayPauseClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
    onPreviousClicked: () -> Unit = {},
    onRepeatClicked: () -> Unit = {},
    onShuffleClicked: () -> Unit = {},
    onFavoriteClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onMoreClicked: () -> Unit = {}
) {

    val context = LocalContext.current
    var currentTime by remember { mutableIntStateOf(0) }
    val themeColor = colorResource(id = R.color.purple_700)

    // LaunchedEffect
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (currentTime < totalDuration) {
                kotlinx.coroutines.delay(1000L)
                currentTime += 1
                Log.i("PROGRSS", "MusicPlayerScreen: $currentTime")
            }
            if (currentTime >= totalDuration) {
                currentTime = totalDuration // اطمینان از رسیدن به انتهای آهنگ
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Row (Back and More Icons)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Image(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "More",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onMoreClicked.invoke() }
            )
        }

        // Album Art
        Image(
            painter = when (songCover) {
                is SongCover.Uri -> rememberImagePainter(songCover.uri)
                is SongCover.Drawable -> painterResource(id = songCover.resId)
            },
            contentDescription = albumName,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // Song Title and Artist Name
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = songTitle,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = artistName,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Music Progress and Timer - Favorite Button and Playlist Add Button
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Favorite Button
            Image(
                painter = painterResource(id = if (isFavorite) R.drawable.favorite else R.drawable.un_favorite),
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onFavoriteClicked.invoke() },
            )

            // Progress Bar
            LinearProgressBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.Transparent),

                colors = SliderColors(
                    thumbColor = themeColor,
                    activeTrackColor = themeColor,
                    inactiveTrackColor = Color.LightGray,
                    disabledThumbColor = Color.LightGray,
                    disabledActiveTrackColor = Color.LightGray,
                    disabledInactiveTrackColor = Color.LightGray,
                    disabledActiveTickColor = Color.LightGray,
                    inactiveTickColor = Color.LightGray,
                    activeTickColor = Color.LightGray,
                    disabledInactiveTickColor = Color.LightGray
                ),

                progress = (currentTime.toFloat() / totalDuration) * 100, // محاسبه درصد پیشرفت ,
                valueRange = 0f..100f,
                onValueChange = { p ->
                    Toast.makeText(context, p.toString(), Toast.LENGTH_SHORT).show()
                },
                onValueChangeFinished = {
                    Toast.makeText(context, "Progress changed", Toast.LENGTH_SHORT).show()
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatTime(currentTime), color = Color.Gray, fontSize = 14.sp) // زمان جاری
                Text(text = formatTime(totalDuration), color = Color.Gray, fontSize = 14.sp) // مدت زمان کل
            }
        }

        // Media Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Shuffle Button
            Image(
                painter = painterResource(id = R.drawable.shuffle),
                contentDescription = "Shuffle",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onShuffleClicked.invoke() },
                colorFilter = ColorFilter.tint(if (isShuffleOn) Color.Blue else Color.Black)
            )

            // Previous Button
            Image(
                painter = painterResource(id = R.drawable.previews),
                contentDescription = "Previous",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onPreviousClicked.invoke() },
                colorFilter = ColorFilter.tint(Color.Black)
            )

            // Play/Pause Button
            Image(
                painter = painterResource(id = if (isPlaying) R.drawable.play else R.drawable.pause),
                contentDescription = "Play/Pause",
                modifier = Modifier
                    .size(82.dp)
                    .clickable { onPlayPauseClicked.invoke() },
                colorFilter = ColorFilter.tint(if (isPlaying) themeColor else colorResource(id = R.color.black))
            )

            // Next Button
            Image(
                painter = painterResource(id = R.drawable.next),
                contentDescription = "Next",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onNextClicked.invoke() },
                colorFilter = ColorFilter.tint(Color.Black)
            )

            // Repeat Button
            Image(
                painter = painterResource(id = R.drawable.repeat),
                contentDescription = "Repeat",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onRepeatClicked.invoke() },
                colorFilter = ColorFilter.tint(if (isRepeatOn) Color.Blue else Color.Black)
            )

        }
    }
}



sealed class SongCover {
    data class Uri(val uri: String) : SongCover()
    data class Drawable(val resId: Int) : SongCover()
}


// Helper function to format time in mm:ss
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}

@Preview(showBackground = true)
@Composable
fun PreviewMusicPlayerScreen() {
    MusicPlayerScreen()
}

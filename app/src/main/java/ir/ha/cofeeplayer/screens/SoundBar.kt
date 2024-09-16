package ir.ha.cofeeplayer.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import ir.ha.cofeeplayer.R
import ir.ha.cofeeplayer.common.ExoPlayerHelper
import ir.ha.cofeeplayer.common.LinearProgressBar


val TAG = "TAG"

@Composable
fun SoundBar(
    isExpanded: Boolean = true,
    exoPlayerHelper: ExoPlayerHelper,
    songUri: Uri?,
    songTitle: String = "Unknown",
    artistName: String = "Unknown",
    albumName: String = "Unknown",
    songCover: SongCover,
    isPlaying: Boolean,
    isMuteOn: Boolean,
    isRepeatOn: Boolean,
    isShuffleOn: Boolean,
    isFavorite: Boolean,
    totalDuration: Int, // music track duration (s)
    onPlayPauseClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
    onPreviousClicked: () -> Unit = {},
    onRepeatClicked: () -> Unit = {},
    onShuffleClicked: () -> Unit = {},
    onFavoriteClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onMoreClicked: () -> Unit = {},
    onMuteClicked: () -> Unit = {},
    onSoundBarClick: () -> Unit = {}
) {


    val context = LocalContext.current // Get context in the composable

    val transition = updateTransition(targetState = isExpanded, label = "")

    val height by transition.animateDp(
        label = "height",
        transitionSpec = { tween(durationMillis = 1000) } // تنظیم زمان انیمیشن
    ) { state ->
        if (state) 400.dp else 80.dp
    }

    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 2000) }
    ) { state ->
        if (state) 1f else 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(height)
            .background(Color.White)
            .clickable { /* تریگر کردن برای باز و بسته شدن */ },
        contentAlignment = Alignment.BottomStart
    ) {
        if (isExpanded) {

            var currentTime by remember { mutableIntStateOf(0) }
            val themeColor = colorResource(id = R.color.purple_700)

            // LaunchedEffect
            LaunchedEffect(isPlaying) {
                if (isPlaying) {
                    while (currentTime < totalDuration) {
                        kotlinx.coroutines.delay(1000L)
                        currentTime += 1
                        Log.i(TAG, "MusicPlayerScreen: $currentTime")
                    }

                    songUri?.let {
                        exoPlayerHelper.initializePlayer(it)
                        exoPlayerHelper.play()
                    }

                    if (currentTime >= totalDuration) {
                        currentTime = totalDuration // sure to finished music track
                        exoPlayerHelper.pause()
                        onPlayPauseClicked()
                        return@LaunchedEffect
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
                Box(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = if (isMuteOn) R.drawable.mute else R.drawable.unmute),
                            contentDescription = "Mute",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = androidx.compose.material3.ripple(
                                        bounded = false, // Borderless ripple
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    onClick = {
                                        onMuteClicked.invoke()
                                    }
                                )
                        )


                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))


                        Image(
                            painter = painterResource(id = R.drawable.more),
                            contentDescription = "More",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = androidx.compose.material3.ripple(
                                        bounded = false, // Borderless ripple
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    onClick = {
                                        onMoreClicked.invoke()
                                    }
                                )
                        )
                    }



                    Image(
                        painter = painterResource(id = R.drawable.back_pressed),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(30.dp)
                            .rotate(-90F)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = androidx.compose.material3.ripple(
                                    bounded = false, // Borderless ripple
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = { onBackClicked.invoke() }
                            )
                    )
                }

                // Album Art
                Image(
                    painter = when (songCover) {
                        is SongCover.Uri -> rememberImagePainter((songCover as SongCover.Uri).uri)
                        is SongCover.Drawable -> painterResource(id = (songCover as SongCover.Drawable).resId)
                    },
                    contentDescription = albumName,
                    modifier = Modifier
                        .background(Color.LightGray, RoundedCornerShape(8))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8))
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
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = androidx.compose.material3.ripple(
                                    bounded = false, // Borderless ripple
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    onFavoriteClicked.invoke()
                                }
                            ),
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
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTime(currentTime),
                            color = Color.Gray,
                            fontSize = 14.sp
                        ) // زمان جاری
                        Text(
                            text = formatTime(totalDuration),
                            color = Color.Gray,
                            fontSize = 14.sp
                        ) // مدت زمان کل
                    }
                }

                // Media Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 34.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Shuffle Button
                    Image(
                        painter = painterResource(id = R.drawable.shuffle),
                        contentDescription = "Shuffle",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = androidx.compose.material3.ripple(
                                    bounded = false, // Borderless ripple
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    onShuffleClicked.invoke()
                                }
                            ),
                        colorFilter = ColorFilter.tint(if (isShuffleOn) Color.Blue else Color.Black)
                    )

                    // Previous Button
                    Image(
                        painter = painterResource(id = R.drawable.previews),
                        contentDescription = "Previous",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = androidx.compose.material3.ripple(
                                    bounded = false, // Borderless ripple
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    onPreviousClicked.invoke()
                                }
                            ),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )

                    // Play/Pause Button
                    Image(
                        painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                        contentDescription = "Play/Pause",
                        modifier = Modifier
                            .size(82.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = androidx.compose.material3.ripple(
                                    bounded = false, // Borderless ripple
                                    color = MaterialTheme.colorScheme.primary
                                ), onClick = {
                                    onPlayPauseClicked.invoke()
                                }
                            ),
                        colorFilter = ColorFilter.tint(
                            if (isPlaying) themeColor else colorResource(
                                id = R.color.black
                            )
                        )
                    )

                    // Next Button
                    Image(
                        painter = painterResource(id = R.drawable.next),
                        contentDescription = "Next",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = androidx.compose.material3.ripple(
                                    bounded = false, // Borderless ripple
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    onNextClicked.invoke()
                                }
                            ),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )

                    // Repeat Button
                    Image(
                        painter = painterResource(id = R.drawable.repeat),
                        contentDescription = "Repeat",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = androidx.compose.material3.ripple(
                                    bounded = false, // Borderless ripple
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    onRepeatClicked.invoke()
                                }
                            ),
                        colorFilter = ColorFilter.tint(if (isRepeatOn) Color.Blue else Color.Black)
                    )

                }
            }
        } else {

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(50))
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(50))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = androidx.compose.material3.ripple(
                            bounded = true, // Borderless ripple
                            color = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            onSoundBarClick.invoke()
                        }
                    ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.cover),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .border(shape = CircleShape, border = BorderStroke(1.dp, Color.LightGray))
                            .padding(
                                8.dp
                            )
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = songTitle,
                            maxLines = 20,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = artistName,
                            maxLines = 20,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.previews),
                        contentDescription = "Previous",
                        Modifier
                            .size(24.dp)
                            .clickable { onPreviousClicked.invoke() }
                    )

                    Spacer(modifier = Modifier.size(24.dp))

                    Image(
                        painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                        contentDescription = "Play/Pause",
                        Modifier
                            .size(28.dp)
                            .clickable { onPlayPauseClicked.invoke() }
                    )

                    Spacer(modifier = Modifier.size(24.dp))

                    Image(
                        painter = painterResource(id = R.drawable.next),
                        contentDescription = "Next",
                        Modifier
                            .size(24.dp)
                            .clickable { onNextClicked.invoke() }
                    )
                }
            }
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
fun Previewsss() {

    val context = LocalContext.current
    val exoPlayerHelper = remember { ExoPlayerHelper(context) }

    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val songTitle by rememberSaveable { mutableStateOf("Song Title") }
    val artistName by rememberSaveable { mutableStateOf("Artist Name") }
    val albumName by rememberSaveable { mutableStateOf("Album Name") }
    val songUri: Uri? = null
    val songCover by remember { mutableStateOf<SongCover>(SongCover.Drawable(R.drawable.cover)) }
    val songDurations by rememberSaveable { mutableIntStateOf(1) }
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var isRepeatOn by rememberSaveable { mutableStateOf(false) }
    var isShuffleOn by rememberSaveable { mutableStateOf(false) }
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var isMuteOn by rememberSaveable { mutableStateOf(false) }


    SoundBar(
        exoPlayerHelper = exoPlayerHelper,
        isExpanded = isExpanded,
        songUri = songUri,
        songTitle = songTitle,
        artistName = artistName,
        albumName = albumName,
        songCover = songCover,
        totalDuration = songDurations,
        isPlaying = isPlaying,
        isRepeatOn = isRepeatOn,
        isShuffleOn = isShuffleOn,
        isFavorite = isFavorite,
        isMuteOn = isMuteOn,
        onPlayPauseClicked = {
            isPlaying = isPlaying.not()
        },
        onNextClicked = {
            Toast.makeText(context, "onNextClicked", Toast.LENGTH_SHORT).show()
        },
        onPreviousClicked = {
            Toast.makeText(context, "onPreviousClicked", Toast.LENGTH_SHORT).show()
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
        }
    )
}
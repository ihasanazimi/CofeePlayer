package ir.ha.cofeeplayer.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import ir.ha.cofeeplayer.R
import ir.ha.cofeeplayer.common.LinearProgressBar
import ir.ha.cofeeplayer.common.formatTime
import ir.ha.cofeeplayer.data.database.SongEntity

@Composable
fun PlayerScreen(
    context: Context,
    songEntity: SongEntity?,
    isPlaying: Boolean,
    currentLeftTime  : Float , /* second */
    isMuteOn: Boolean,
    isFavorite: Boolean,
    isShuffleOn: Boolean,
    isRepeatOn: Boolean,
    onMuteClicked: () -> Unit,
    onMoreClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onRepeatClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPlayPauseClicked :  () -> Unit,
    sliderPosition : (Float) -> Unit ,
    isUserSliding  : (Boolean) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
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
                            indication = ripple(
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
                            indication = ripple(
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
                        indication = ripple(
                            bounded = false, // Borderless ripple
                            color = MaterialTheme.colorScheme.primary
                        ),
                        onClick = { onBackClicked.invoke() }
                    )
            )
        }

        // Album Art
        @OptIn(ExperimentalCoilApi::class)
        (Image(
        painter = rememberImagePainter(data = songEntity?.songCover),
        contentDescription = songEntity?.songAlbum,
        modifier = Modifier
            .background(Color.LightGray, RoundedCornerShape(8))
            .border(1.dp, Color.Gray, RoundedCornerShape(8))
            .size(200.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    ))

        // Song Title and Artist Name
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = songEntity?.songTitle?:"",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = songEntity?.songArtist?:"",
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
                        indication = ripple(
                            bounded = false, // Borderless ripple
                            color = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            onFavoriteClicked.invoke()
                        }
                    ),
            )

            Log.i(TAG, "PlayerScreen: ${currentLeftTime}")
            // Progress Bar
            LinearProgressBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.Transparent),

                colors = SliderColors(
                    thumbColor = Color.Blue,
                    activeTrackColor = Color.Blue,
                    inactiveTrackColor = Color.LightGray,
                    disabledThumbColor = Color.LightGray,
                    disabledActiveTrackColor = Color.LightGray,
                    disabledInactiveTrackColor = Color.LightGray,
                    disabledActiveTickColor = Color.LightGray,
                    inactiveTickColor = Color.LightGray,
                    activeTickColor = Color.LightGray,
                    disabledInactiveTickColor = Color.LightGray
                ),
                progress = (currentLeftTime / (songEntity?.songDuration?:0)) * 100, // محاسبه درصد پیشرفت ,
                valueRange = 0f..100f,
                onValueChange = { p ->
                    Log.i(TAG, "PlayerScreen - onValueChange ${p}")
                    isUserSliding.invoke(true)
                    sliderPosition.invoke(p)
                },
                onValueChangeFinished = {
                    Log.i(TAG, "PlayerScreen - onValueChangeFinished")
                    isUserSliding.invoke(false)
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(currentLeftTime.toInt()),
                    color = Color.Gray,
                    fontSize = 14.sp
                ) // زمان جاری
                Text(
                    text = formatTime(songEntity?.songDuration?:0),
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
                        indication = ripple(
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
                        indication = ripple(
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
                        indication = ripple(
                            bounded = false, // Borderless ripple
                            color = MaterialTheme.colorScheme.primary
                        ), onClick = {
                            onPlayPauseClicked.invoke()
                        }
                    ),
                colorFilter = ColorFilter.tint(
                    if (isPlaying) Color.Blue else colorResource(
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
                        indication = ripple(
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
                        indication = ripple(
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
}
package ir.ha.cofeeplayer.screens

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import ir.ha.cofeeplayer.R
import ir.ha.cofeeplayer.data.database.SongEntity


@Composable
fun SoundBar(
    isPlaying: Boolean,
    selectedSong : Pair<List<SongEntity>,Int>,
    onPlayPauseClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
    onPreviousClicked: () -> Unit = {},
    onSoundBarClick: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .background(color = Color.Transparent, shape = RoundedCornerShape(50))
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val cover = if (selectedSong.first[selectedSong.second].songCover.isNullOrEmpty().not()) {
                rememberImagePainter(data = selectedSong.first[selectedSong.second].songCover)
            } else {
                painterResource(id = R.drawable.cover)
            }


            Image(
                painter = cover,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .border(
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color.LightGray)
                    )
                    .padding(
                        8.dp
                    )
            )


            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = selectedSong.first[selectedSong.second].songTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = selectedSong.first[selectedSong.second].songArtist,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))


            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {



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
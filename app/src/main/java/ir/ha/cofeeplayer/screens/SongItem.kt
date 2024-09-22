package ir.ha.cofeeplayer.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import ir.ha.cofeeplayer.R
import ir.ha.cofeeplayer.common.BaseLazyColumn
import ir.ha.cofeeplayer.data.database.SongEntity


@Composable
fun SongListScreen(selectedSong : Pair<List<SongEntity>,Int>, onSongClick: (Int) -> Unit = { _ -> }) {

    Box(modifier = Modifier.fillMaxWidth()) {
        BaseLazyColumn(items = selectedSong.first, onItemClick = { song, index -> onSongClick.invoke(index) }) { item, index ->
            Row(modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()) {

                val cover = if (item.songCover.isNullOrEmpty().not()) {
                        rememberImagePainter(data = item.songCover)
                    } else {
                        painterResource(id = R.drawable.cover)
                    }


                Image(
                    painter = cover,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(8.dp)
                )


                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = item.songTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = item.songArtist,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }


            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SongsScreenPreview(onSongClick: (Int) -> Unit = { _ -> }) {

    val songItems = listOf<SongEntity>(
        SongEntity(
            id = 1,
            songTitle = "Gonjesh qanari shode",
            songArtist = "Sobhan",
            songAlbum = "Gonjish",
            songCover = "https://cdn-icons-png.freepik.com/512/1987/1987912.png",
            songDuration = 2,
            songUri = Uri.parse("https://cdn-icons-png.freepik.com/512/1987/1987912.png"),
            isFavorite = false
        ),

        SongEntity(
            id = 1,
            songTitle = "Hasan Azimi",
            songArtist = "Hsn",
            songAlbum = "chap",
            songCover = "",
            songDuration = 8,
            songUri = Uri.parse("https://cdn-icons-png.freepik.com/512/1987/1987912.png"),
            isFavorite = false
        ),

        )

    Surface {
        SongListScreen(Pair(songItems,0), onSongClick = { pos ->
            onSongClick.invoke(pos)
        })
    }
}
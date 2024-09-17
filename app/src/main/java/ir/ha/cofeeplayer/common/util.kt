package ir.ha.cofeeplayer.common

import android.net.Uri
import ir.ha.cofeeplayer.data.database.SongEntity


// Helper function to format time in mm:ss
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}




val songItems = listOf<SongEntity>(
    SongEntity(
        id = 1,
        songTitle = "Gonjesh qanari shode",
        songArtist = "Sobhan",
        songAlbum = "Gonjish",
        songCover = "https://cdn-icons-png.freepik.com/512/1987/1987912.png",
        songDuration = 2,
        songUrl = Uri.parse("https://cdn-icons-png.freepik.com/512/1987/1987912.png"),
        isFavorite = false
    ),

    SongEntity(
        id = 1,
        songTitle = "Hasan Azimi",
        songArtist = "Hsn",
        songAlbum = "chap",
        songCover = "",
        songDuration = 8,
        songUrl = Uri.parse("https://cdn-icons-png.freepik.com/512/1987/1987912.png"),
        isFavorite = false
    ),

    )
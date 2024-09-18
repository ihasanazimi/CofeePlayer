package ir.ha.cofeeplayer.data.database

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class SongEntity (
    @PrimaryKey
    @ColumnInfo
    var id : Long = -1,
    @ColumnInfo
    var songUri : Uri,
    @ColumnInfo
    var songTitle : String = "Unknown",
    @ColumnInfo
    var songArtist : String = "Unknown",
    @ColumnInfo
    var songAlbum : String = "Unknown",
    @ColumnInfo
    var songCover : String,
    @ColumnInfo
    var songDuration : Int = 0,
    @ColumnInfo
    var isFavorite : Boolean = false,
) : Parcelable
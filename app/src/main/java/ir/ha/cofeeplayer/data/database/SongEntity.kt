package ir.ha.cofeeplayer.data.database

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class SongEntity (
    var id : Long = -1 ,
    var songUrl : Uri,
    var songTitle : String ,
    var songArtist : String ,
    var songAlbum : String ,
    var songCover : String,
    var songDuration : Int ,
    var isFavorite : Boolean = false,
) : Parcelable
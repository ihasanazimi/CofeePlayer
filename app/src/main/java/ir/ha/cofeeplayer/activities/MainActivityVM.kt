package ir.ha.cofeeplayer.activities

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ha.cofeeplayer.common.BaseViewModel
import ir.ha.cofeeplayer.data.database.RoomDB
import ir.ha.cofeeplayer.data.database.SongEntity
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityVM @Inject constructor(
    private val roomDB: RoomDB
) : BaseViewModel<List<SongEntity>>() {

    fun fetchSongs() {
        viewModelScope.launch {
            try {
                setLoading(true)
                val songs = roomDB.songDto().getAllSong()
                setData(songs.ifEmpty { arrayListOf() })
                setLoading(false)
            } catch (e: Exception) {
                setLoading(false)
            }
        }
    }

}
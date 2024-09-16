package ir.ha.cofeeplayer.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import ir.ha.cofeeplayer.screens.SongCover


@Database(
    entities = [SongEntity::class],
    version = 1,
    exportSchema = false
)

//@TypeConverters(Converters::class)
abstract class RoomDB : RoomDatabase() {

    /** Dao */
    abstract fun songDto(): SongDto

    companion object {

        private val RoomDB_TAG = this::class.java.simpleName

        @Volatile //access just one there on main thread!
        var database: RoomDB? = null

        // singleTon design pattern
        fun getDataBase(context: Context): RoomDB {
            val tempInstance = database
            if (database != null) return tempInstance as RoomDB
            //synchronized  -->  means -->  access just one there on main thread!
            synchronized(this) {
                val instance = Room.databaseBuilder(context, RoomDB::class.java, "song_data_base")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                database = instance
                return instance.also {
                    Log.i(RoomDB_TAG, "getDataBase: $it")
                }
            }
        }
    }
}




interface SongDto {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(song: SongEntity)

    @Query("SELECT * FROM SongEntity;")
    suspend fun getAllSong() : List<SongCover>

    @Delete
    suspend fun deleteSong(song: SongEntity)

    @Update
    suspend fun updateSong(song: SongEntity)

    @Query("DELETE FROM SongEntity")
    suspend fun deleteAllSong()

    @Query("SELECT * FROM SongEntity WHERE songTitle LIKE '%' || :songTitle || '%' or songArtist LIKE '%' || :songArtist || '%' or songAlbum LIKE '%' || :songAlbum || '%'")
    suspend fun findSong(songTitle : String , songArtist : String , songAlbum : String) : List<SongEntity>


}
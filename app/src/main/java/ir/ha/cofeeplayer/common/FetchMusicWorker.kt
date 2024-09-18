package ir.ha.cofeeplayer.common

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import ir.ha.cofeeplayer.activities.MyApplication
import ir.ha.cofeeplayer.data.database.SongEntity
import okio.IOException
import java.util.concurrent.TimeUnit

class FetchMusicWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    val TAG = FetchMusicWorker::class.java.simpleName

    override suspend fun doWork(): Result {
        try {
            val songList = fetchAllMusic(context.contentResolver)
            MyApplication.roomDB.songDto().addNewList(songList)
        }catch (e : IOException){
           return Result.failure()
        }
        return Result.success()
    }

    private suspend fun fetchAllMusic(contentResolver: ContentResolver): List<SongEntity> {

        Log.i(TAG, "fetchAllMusic: ")

        val songList = mutableListOf<SongEntity>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,          // برای استخراج URI فایل
            MediaStore.Audio.Media.TITLE,        // عنوان موزیک
            MediaStore.Audio.Media.ARTIST,       // نام هنرمند
            MediaStore.Audio.Media.ALBUM,        // نام آلبوم
            MediaStore.Audio.Media.DURATION,     // مدت زمان موزیک
            MediaStore.Audio.Media.DATA,         // مسیر فایل موزیک
            MediaStore.Audio.Media.ALBUM_ID      // ID آلبوم
        )

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val title = it.getString(titleIndex)
                val artist = it.getString(artistIndex)
                val album = it.getString(albumIndex)
                val duration = it.getInt(durationIndex) / 1000
                val songUrl = Uri.parse(it.getString(dataIndex))

                val albumArtUri = Uri.withAppendedPath(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    it.getString(albumIdIndex)  // استفاده از albumIdIndex به جای ستون از پیش موجود
                ).toString()

                val song = SongEntity(
                    id = id,
                    songUri = songUrl,
                    songTitle = title,
                    songArtist = artist,
                    songAlbum = album,
                    songCover = albumArtUri,  // URI کاور آلبوم
                    songDuration = duration ,
                    isFavorite = false  // به صورت پیش‌فرض false است، می‌توانید به دلخواه تغییر دهید
                )

                songList.add(song)
                Log.i(TAG, "fetchAllMusic: $song")
            }
        }

        return songList
    }
}


fun scheduleFetchMusicWork(context: Context) {
    val fetchSongsWork = PeriodicWorkRequestBuilder<FetchMusicWorker>(1, TimeUnit.MINUTES).build()
    WorkManager.getInstance(context).enqueue(fetchSongsWork)
}


package ir.ha.cofeeplayer.common

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class FetchMusicWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    val TAG = FetchMusicWorker::class.java.simpleName

    override fun doWork(): Result {
        // لیست موزیک‌ها را فچ کنید
        val musicList = fetchAllMusic(applicationContext.contentResolver)

        // اینجا می‌توانید لیست موزیک‌ها را در دیتابیس یا جایی ذخیره کنید
        // همچنین، می‌توانید با Notification لیست را به کاربر نشان دهید

        return Result.success()
    }

    private fun fetchAllMusic(contentResolver: ContentResolver): List<String> {
        val musicList = mutableListOf<String>()
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA
        )

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex)
                val data = it.getString(dataIndex)
                musicList.add("$title - $data")
            }
        }

        return musicList.also {
            Log.i(TAG, "fetchAllMusic $it: ")
        }
    }
}


fun scheduleFetchMusicWork(context: Context) {
    val fetchMusicWork = PeriodicWorkRequestBuilder<FetchMusicWorker>(1, TimeUnit.HOURS)
        .build()

    WorkManager.getInstance(context).enqueue(fetchMusicWork)
}


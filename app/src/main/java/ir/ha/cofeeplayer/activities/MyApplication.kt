package ir.ha.cofeeplayer.activities

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import ir.ha.cofeeplayer.common.scheduleFetchMusicWork
import ir.ha.cofeeplayer.data.database.RoomDB


@HiltAndroidApp
class MyApplication : MultiDexApplication() {


    companion object{
        lateinit var roomDB: RoomDB
    }

    override fun onCreate() {
        super.onCreate()
        roomDB = RoomDB.getDataBase(this)

        scheduleFetchMusicWork(this)

    }
}
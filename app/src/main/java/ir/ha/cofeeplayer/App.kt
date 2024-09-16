package ir.ha.cofeeplayer

import android.app.Application
import ir.ha.cofeeplayer.common.scheduleFetchMusicWork
import ir.ha.cofeeplayer.data.database.RoomDB

class App : Application() {


    companion object{
        lateinit var roomDB: RoomDB
    }

    override fun onCreate() {
        super.onCreate()
        roomDB = RoomDB.getDataBase(this)

        scheduleFetchMusicWork(applicationContext)

    }
}
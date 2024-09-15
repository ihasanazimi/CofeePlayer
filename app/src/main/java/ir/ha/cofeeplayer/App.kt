package ir.ha.cofeeplayer

import android.app.Application
import ir.ha.cofeeplayer.common.scheduleFetchMusicWork

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleFetchMusicWork(applicationContext)
    }
}
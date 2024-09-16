package ir.ha.cofeeplayer.di

import dagger.Provides
import ir.ha.cofeeplayer.App
import ir.ha.cofeeplayer.data.database.RoomDB
import javax.inject.Singleton

object CatchModules {


    @Singleton
    @Provides
    fun provideRoomDB() : RoomDB {
        return App.roomDB
    }

}
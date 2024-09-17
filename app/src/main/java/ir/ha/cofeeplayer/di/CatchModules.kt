package ir.ha.cofeeplayer.di

import androidx.annotation.Keep
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.ha.cofeeplayer.activities.MyApplication
import ir.ha.cofeeplayer.data.database.RoomDB
import javax.inject.Singleton


@Keep
@Module
@InstallIn(SingletonComponent::class)
object CatchModules {


    @Singleton
    @Provides
    fun provideRoomDB() : RoomDB {
        return MyApplication.roomDB
    }

}
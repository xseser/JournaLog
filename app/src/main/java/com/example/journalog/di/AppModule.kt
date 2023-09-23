package com.example.journalog.di

import android.content.Context
import androidx.room.Room
import com.example.journalog.database.JournaLogRoomDatabase
import com.example.journalog.database.LogBookDao
import com.example.journalog.database.LogDao
import com.example.journalog.datastore.DataStoreRepository
import com.example.journalog.datastore.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    fun provideApplicationContext(app: Application) {
//        return
//    }

    @Provides
    fun provideLogBookDao(appDatabase: JournaLogRoomDatabase): LogBookDao {
        return appDatabase.logBookDao
    }

    @Provides
    fun provideLogDao(appDatabase: JournaLogRoomDatabase): LogDao {
        return appDatabase.logDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): JournaLogRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            JournaLogRoomDatabase::class.java,
            "journalog.db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository = DataStoreRepositoryImpl(context)

}
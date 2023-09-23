package com.example.journalog.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(LogBook::class), (SingleLog::class)], version = 4, exportSchema = false)
abstract class JournaLogRoomDatabase : RoomDatabase() {

    abstract val logBookDao: LogBookDao
    abstract val logDao: LogDao

//    abstract fun logDao(): LogDao

//    companion object {
//        /*The value of a volatile variable will never be cached, and all writes and reads will be done to and from the main memory.
//        This helps make sure the value of INSTANCE is always up-to-date and the same for all execution threads.
//        It means that changes made by one thread to INSTANCE are visible to all other threads immediately.*/
//        @Volatile
//        private var INSTANCE: JournaLogRoomDatabase? = null
//
//        fun getInstance(context: Context): JournaLogRoomDatabase {
//            // only one thread of execution at a time can enter this block of code
//            synchronized(this) {
//                var instance = INSTANCE
//
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        JournaLogRoomDatabase::class.java,
//                        "employee_database"
//                    ).fallbackToDestructiveMigration()
//                        .build()
//
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    }
}
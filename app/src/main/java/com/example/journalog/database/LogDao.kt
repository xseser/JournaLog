package com.example.journalog.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLog(singleLog: SingleLog)

    @Query("SELECT * FROM user_logs")
    fun getAllLogs(): Flow<List<SingleLog>>

    @Query("SELECT user_logs.* FROM user_logs INNER JOIN log_book ON user_logs.log_book_id = log_book.id WHERE log_book.active = 1")
    fun getAllLogsFromActiveLogBook(): Flow<List<SingleLog>>

    @Query("SELECT * FROM user_logs WHERE log_book_id = :logBookId")
    fun getLogsByLogBookId(logBookId: Int): Flow<List<SingleLog>>

    @Update
    suspend fun updateLog(singleLog: SingleLog)

    @Delete
    suspend fun deleteLog(singleLog: SingleLog)

    @Query("DELETE FROM user_logs WHERE log_book_id = :logBookId")
    suspend fun deleteLogsByLogBookId(logBookId: Int)
}
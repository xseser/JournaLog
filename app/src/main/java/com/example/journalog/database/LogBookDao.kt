package com.example.journalog.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LogBookDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLogBook(logBook: LogBook)

    @Query("SELECT * FROM log_book")
    fun getAllLogBooks(): Flow<List<LogBook>>

    @Query("SELECT * FROM log_book WHERE id = :logBookId")
    fun getLogBookById(logBookId: Int): LogBook

    @Query("SELECT EXISTS (SELECT 1 FROM log_book WHERE active = 1)")
    fun isAnyLogBookActive() : Boolean

    @Query("SELECT * FROM log_book WHERE active = 1")
    fun getActiveLogBook(): LogBook?

    @Query("SELECT * FROM log_book WHERE active = 1")
    fun getActiveLogBookAsFlow(): Flow<LogBook>

    @Query("SELECT id FROM log_book WHERE active = 1")
    fun getActiveLogBookId(): Int

    @Query("SELECT name FROM log_book WHERE active = 1")
    fun getActiveLogBookTitle(): String

//    @Query("UPDATE log_book SET active = :isActive WHERE id = :logBookId")
//    suspend fun setActiveLogBook(logBookId: Int, isActive: Boolean)

    @Query("UPDATE log_book SET active = 0")
    suspend fun setAllInactive()

    @Query("UPDATE log_book SET active = CASE WHEN id = :logBookId THEN 1 ELSE 0 END")
    suspend fun setActiveLogBook(logBookId: Int)

    @Update
    suspend fun updateLogBook(logBook: LogBook)

    @Delete
    suspend fun deleteLogBook(logBook: LogBook)
}
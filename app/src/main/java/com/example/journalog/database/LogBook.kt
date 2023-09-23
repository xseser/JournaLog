package com.example.journalog.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_book")
data class LogBook(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "start_time")
    var startTime: Long = 0L,

    @ColumnInfo(name = "active")
    var active: Boolean = false

//    @ColumnInfo(name = "log_list")
//    var logList: List<SingleLog>
)

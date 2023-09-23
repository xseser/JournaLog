package com.example.journalog.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//@Parcelize
@Entity(tableName = "user_logs")
data class SingleLog(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "log_book_id")
    var logBookId: Int,

//    @ColumnInfo(name = "time")
//    var time: String,

    @ColumnInfo(name = "time_when_sent")
    var timeWhenSent: Long,

    @ColumnInfo(name = "content")
    var content: String
)
//) : Parcelable

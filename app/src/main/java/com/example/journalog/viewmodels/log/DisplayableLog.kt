package com.example.journalog.viewmodels.log

import com.example.journalog.database.SingleLog
import com.example.journalog.model.SingleLogResponseModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds

data class DisplayableLog(
    var id: Int = 0,
    var logBookId: Int,
    var timeWhenSent: Long,
    var content: String,
    var dispTimePassed: String,
    var dispTimeAbsolute: String,
    var isAnchor: Boolean = false
) {
//    fun updateDispTimePassed(newStartTime: Long) {
//        val passedTimeMillis = timeWhenSent - newStartTime
//        val passedTimeSeconds = (passedTimeMillis / 1000).seconds
//        dispTimePassed = "After $passedTimeSeconds"
//    }
}

fun SingleLog.toDisplayableLog(startTime: Long): DisplayableLog {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
    val timeWhenSentFormatted = sdf.format(timeWhenSent)
    val passedTimeMillis = (timeWhenSent - startTime).absoluteValue
    val passedTimeSeconds = (passedTimeMillis / 1000).seconds
    val dispTimePassed = if (timeWhenSent - startTime > 0) {
        "After $passedTimeSeconds"
    } else {
        "$passedTimeSeconds before"
    }
    val dispTimeAbsolute = "($timeWhenSentFormatted)"

    return DisplayableLog(
        this.id,
        this.logBookId,
        this.timeWhenSent,
        this.content,
        dispTimePassed,
        dispTimeAbsolute
    )
}

fun SingleLogResponseModel.toDisplayableLog(startTime: Long): DisplayableLog {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
    val timeWhenSentFormatted = sdf.format(sentTime.toLong())
    val passedTimeMillis = (sentTime.toLong() - startTime).absoluteValue
    val passedTimeSeconds = (passedTimeMillis / 1000).seconds
    val dispTimePassed = if (sentTime.toLong() - startTime > 0) {
        "After $passedTimeSeconds"
    } else {
        "$passedTimeSeconds before"
    }
    val dispTimeAbsolute = "($timeWhenSentFormatted)"

    return DisplayableLog(
        this.id,
//        this.logBookId,
        -1,
        this.sentTime.toLong(),
        this.content,
        dispTimePassed,
        dispTimeAbsolute
    )
}
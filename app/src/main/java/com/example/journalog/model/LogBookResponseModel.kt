package com.example.journalog.model

import com.google.gson.annotations.SerializedName

data class LogBookResponseModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("startTime")
    var startTime: String,
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("active")
    var active: Boolean
)

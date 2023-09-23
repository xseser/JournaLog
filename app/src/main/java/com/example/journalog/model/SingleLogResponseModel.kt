package com.example.journalog.model

import com.google.gson.annotations.SerializedName

data class SingleLogResponseModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("sentTime")
    var sentTime: String,
    @SerializedName("content")
    var content: String
)

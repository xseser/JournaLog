package com.example.journalog.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class User(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("createdAt")
    var createdAt: String
)
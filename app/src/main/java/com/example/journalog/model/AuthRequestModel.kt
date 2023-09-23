package com.example.journalog.model

import com.google.gson.annotations.SerializedName

data class AuthRequestModel(
    @SerializedName("name")
    var name: String,
    @SerializedName("password")
    var password: String
)
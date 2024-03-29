package com.github.mjaremczuk.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Channel (
    val type: String,
    val id: String
)
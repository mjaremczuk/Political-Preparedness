package com.github.mjaremczuk.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Error(
        val code: Int,
        val message: String
)
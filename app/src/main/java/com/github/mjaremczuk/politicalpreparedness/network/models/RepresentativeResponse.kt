package com.github.mjaremczuk.politicalpreparedness.network.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepresentativeResponse(
        val offices: List<Office>,
        val officials: List<Official>
)
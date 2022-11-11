package com.github.mjaremczuk.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Division(
        val id: String,
        val country: String,
        val state: String
) : Parcelable
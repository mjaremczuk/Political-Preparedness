package com.github.mjaremczuk.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Official (
        val name: String,
        val address: List<Address>? = null,
        val party: String? = null,
        val phones: List<String>? = null,
        val urls: List<String>? = null,
        val photoUrl: String? = null,
        val channels: List<Channel>? = null
)
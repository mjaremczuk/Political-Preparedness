package com.github.mjaremczuk.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class State (
    val name: String,
    val electionAdministrationBody: AdministrationBody
)
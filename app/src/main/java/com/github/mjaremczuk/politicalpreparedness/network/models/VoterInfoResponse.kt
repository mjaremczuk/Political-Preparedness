package com.github.mjaremczuk.politicalpreparedness.network.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse (
    val election: Election,
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null
)
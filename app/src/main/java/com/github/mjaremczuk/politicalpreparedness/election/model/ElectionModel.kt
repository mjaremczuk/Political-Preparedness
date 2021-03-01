package com.github.mjaremczuk.politicalpreparedness.election.model

import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import java.util.*

data class ElectionModel(
        val id: Int,
        val name: String,
        val electionDay: Date,
        val division: Division
)
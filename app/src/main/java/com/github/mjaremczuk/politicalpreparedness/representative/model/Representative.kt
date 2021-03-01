package com.github.mjaremczuk.politicalpreparedness.representative.model

import com.github.mjaremczuk.politicalpreparedness.network.models.Office
import com.github.mjaremczuk.politicalpreparedness.network.models.Official

data class Representative (
        val official: Official,
        val office: Office
)
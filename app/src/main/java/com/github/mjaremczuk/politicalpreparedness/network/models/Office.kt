package com.github.mjaremczuk.politicalpreparedness.network.models

import androidx.annotation.Keep
import com.github.mjaremczuk.politicalpreparedness.representative.model.Representative
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Office (
    val name: String,
    @Json(name="divisionId") val division:Division,
    @Json(name="officialIndices") val officials: List<Int>
) {
    fun getRepresentatives(officials: List<Official>): List<Representative> {
        return this.officials.map { index ->
            Representative(officials[index], this)
        }
    }
}

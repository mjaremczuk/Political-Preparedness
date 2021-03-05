package com.github.mjaremczuk.politicalpreparedness.network.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name = "ocdDivisionId", ) val division: Division
)

fun List<Election>.toDomainModel(): List<ElectionModel> {
    return map {
        ElectionModel(
                it.id,
                it.name,
                it.electionDay,
                it.division
        )
    }
}
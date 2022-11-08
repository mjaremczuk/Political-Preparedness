package com.github.mjaremczuk.politicalpreparedness.election.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import com.github.mjaremczuk.politicalpreparedness.network.models.Election
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
data class ElectionModel(
        val id: Int,
        val name: String,
        val electionDay: Date,
        val division: Division,
        val saved: Boolean
) : Parcelable

fun ElectionModel.toDataModel() =
        Election(
                id = id,
                name = name,
                electionDay = electionDay,
                division = division,
                saved = saved
        )

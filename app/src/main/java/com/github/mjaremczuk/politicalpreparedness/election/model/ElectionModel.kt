package com.github.mjaremczuk.politicalpreparedness.election.model

import android.os.Parcelable
import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ElectionModel(
        val id: Int,
        val name: String,
        val electionDay: Date,
        val division: Division
): Parcelable
package com.github.mjaremczuk.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mjaremczuk.politicalpreparedness.database.ElectionDao
import com.github.mjaremczuk.politicalpreparedness.network.models.Division
import kotlinx.coroutines.launch

class VoterInfoViewModel(
        private val dataSource: ElectionDao,
        private val electionId: Int,
        private val division: Division
) : ViewModel() {

    init {
        viewModelScope.launch {
            tmpAddOrRemove()
        }
    }

    private suspend fun tmpAddOrRemove() {
        val election = dataSource.get(electionId, division.id)
        if (election != null) {
            dataSource.save(election.copy(saved = election.saved.not()))
        }
    }

    fun refresh() {
        viewModelScope.launch {
            tmpAddOrRemove()
        }
    }

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}
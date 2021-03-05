package com.github.mjaremczuk.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mjaremczuk.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun getAll(): LiveData<List<Election>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg elections: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(election: Election)
    //TODO: Add insert query

    //TODO: Add select all election query

    //TODO: Add select single election query

    //TODO: Add delete query

    //TODO: Add clear query

}
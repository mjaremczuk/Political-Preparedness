package com.github.mjaremczuk.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mjaremczuk.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun getAll(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun getAllSync(): List<Election>

    @Query("DELETE FROM election_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(election: List<Election>)

    @Query("SELECT * FROM election_table WHERE id = :id AND division_id = :divisionId LIMIT 1")
    suspend fun get(id: Int, divisionId: String): Election?
    //TODO: Add insert query

    //TODO: Add select all election query

    //TODO: Add select single election query

    //TODO: Add delete query

    //TODO: Add clear query

}
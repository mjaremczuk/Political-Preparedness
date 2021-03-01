package com.github.mjaremczuk.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mjaremczuk.politicalpreparedness.DataBindFragment
import com.github.mjaremczuk.politicalpreparedness.databinding.FragmentElectionBinding

class ElectionsFragment : DataBindFragment<FragmentElectionBinding>() {

    //TODO: Declare ViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentElectionBinding.inflate(layoutInflater, container, false)
        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //TODO: Refresh adapters when fragment loads

}
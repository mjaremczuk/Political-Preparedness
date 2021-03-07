package com.github.mjaremczuk.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.mjaremczuk.politicalpreparedness.DataBindFragment
import com.github.mjaremczuk.politicalpreparedness.databinding.FragmentElectionBinding
import com.github.mjaremczuk.politicalpreparedness.election.adapter.ElectionListAdapter
import org.koin.android.ext.android.inject

class ElectionsFragment : DataBindFragment<FragmentElectionBinding>() {

    private val viewModel: ElectionsViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentElectionBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.upcomingElectionsRecycler.adapter =
                ElectionListAdapter(ElectionListAdapter.ElectionListener {
                    viewModel.onUpcomingClicked(it)
                })

        binding.savedElectionsRecycler.adapter =
                ElectionListAdapter(ElectionListAdapter.ElectionListener {
                    viewModel.onSavedClicked(it)
                })
        binding.upcomingRefresh.setOnRefreshListener { viewModel.refresh() }

        viewModel.navigateTo.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.navigateCompleted()
                findNavController().navigate(it)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //TODO: Refresh adapters when fragment loads

}
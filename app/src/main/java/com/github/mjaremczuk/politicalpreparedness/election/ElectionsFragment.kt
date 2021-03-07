package com.github.mjaremczuk.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.github.mjaremczuk.politicalpreparedness.DataBindFragment
import com.github.mjaremczuk.politicalpreparedness.databinding.FragmentElectionBinding
import com.github.mjaremczuk.politicalpreparedness.election.adapter.ElectionListAdapter
import com.github.mjaremczuk.politicalpreparedness.utils.LocationPermissionsUtil
import org.koin.android.ext.android.inject

class ElectionsFragment : DataBindFragment<FragmentElectionBinding>(), LocationPermissionsUtil.PermissionListener {

    private val viewModel: ElectionsViewModel by inject()

    private val permissionUtil = LocationPermissionsUtil(this)

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
        permissionUtil.requestPermissions(this)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGranted() {

    }

    override fun onDenied() {
        Toast.makeText(requireContext(),"Location permission denied!", Toast.LENGTH_LONG).show()
    }
}
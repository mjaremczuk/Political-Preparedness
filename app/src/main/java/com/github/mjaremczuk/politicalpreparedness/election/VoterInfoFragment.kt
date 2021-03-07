package com.github.mjaremczuk.politicalpreparedness.election

import android.location.Geocoder
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mjaremczuk.politicalpreparedness.DataBindFragment
import com.github.mjaremczuk.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.github.mjaremczuk.politicalpreparedness.utils.LocationPermissionsUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VoterInfoFragment : DataBindFragment<FragmentVoterInfoBinding>(), LocationPermissionsUtil.PermissionListener {

    private val permissionUtil = LocationPermissionsUtil(this)
    private val params: VoterInfoFragmentArgs by navArgs()
    private val viewModel: VoterInfoViewModel by viewModel { parametersOf(params.election) }

    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentVoterInfoBinding.inflate(layoutInflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.stateLocations.movementMethod = LinkMovementMethod.getInstance()
        binding.stateBallot.movementMethod = LinkMovementMethod.getInstance()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        permissionUtil.requestPermissions(this)

        viewModel.navigateBack.observe(viewLifecycleOwner) { navigateBack ->
            if (navigateBack) {
                viewModel.navigateCompleted()
                findNavController().popBackStack()
            }
        }
        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */

        //TODO: Handle loading of URLs

        return binding.root
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onGranted() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        try {
                            val address = Geocoder(requireContext()).getFromLocation(it.latitude, it.longitude, 1).firstOrNull()
                            viewModel.loadDetails(address)
                        } catch (ex: Exception) {

                        }
                    }
                }
    }

    override fun onDenied() {
        Toast.makeText(requireContext(), "Location permission denied!", Toast.LENGTH_LONG).show()
    }

    //TODO: Create method to load URL intents

}
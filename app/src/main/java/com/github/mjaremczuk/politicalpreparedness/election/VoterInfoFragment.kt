package com.github.mjaremczuk.politicalpreparedness.election

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mjaremczuk.politicalpreparedness.DataBindFragment
import com.github.mjaremczuk.politicalpreparedness.R
import com.github.mjaremczuk.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.github.mjaremczuk.politicalpreparedness.utils.GeocoderHelper
import com.github.mjaremczuk.politicalpreparedness.utils.LocationPermissionsUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.DateFormat

class VoterInfoFragment : DataBindFragment<FragmentVoterInfoBinding>(),
    LocationPermissionsUtil.PermissionListener {

    private val permissionUtil = LocationPermissionsUtil(this)
    private val params: VoterInfoFragmentArgs by navArgs()
    private val viewModel: VoterInfoViewModel by viewModel { parametersOf(params.election) }
    val geocoderHelperFactory: GeocoderHelper.Factory by inject()

    lateinit var fusedLocationClient: FusedLocationProviderClient

    val dateFormatter: DateFormat by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoterInfoBinding.inflate(layoutInflater, container, false)

        binding.dateFormatter = dateFormatter
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.stateLocations.movementMethod = LinkMovementMethod.getInstance()
        binding.stateBallot.movementMethod = LinkMovementMethod.getInstance()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel.navigateBack.observe(viewLifecycleOwner) { navigateBack ->
            if (navigateBack) {
                viewModel.navigateCompleted()
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showSnackbar(getString(it))
                binding.addressGroup.visibility = View.GONE
                binding.stateGroup.visibility = View.GONE
            }
        }

        viewModel.electionDetails.observe(viewLifecycleOwner) { state ->
            state?.let {
                binding.constraintLayout.transitionToEnd()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionUtil.registerForResultAndRequestPermissions(this)
    }

    override fun onDestroyView() {
        permissionUtil.unregister()
        super.onDestroyView()
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onGranted() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    try {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val address = geocoderHelperFactory.create(requireContext())
                                .getAddressFromLocation(it)
                            viewModel.loadDetails(address)
                        }
                    } catch (ex: Exception) {
                        showSnackbar(getString(R.string.error_failed_get_address_from_location))
                        ex.printStackTrace()
                    }
                }
            }
    }

    override fun onDenied() {
        showSnackbar(getString(R.string.error_location_permission_denied))
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}
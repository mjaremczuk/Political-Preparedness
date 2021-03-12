package com.github.mjaremczuk.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.annotation.RequiresPermission
import com.github.mjaremczuk.politicalpreparedness.DataBindFragment
import com.github.mjaremczuk.politicalpreparedness.R
import com.github.mjaremczuk.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.github.mjaremczuk.politicalpreparedness.network.models.Address
import com.github.mjaremczuk.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.github.mjaremczuk.politicalpreparedness.utils.LocationPermissionsUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DetailFragment : DataBindFragment<FragmentRepresentativeBinding>(), LocationPermissionsUtil.PermissionListener {

    private val permissionUtil = LocationPermissionsUtil(this)
    lateinit var fusedLocationClient: FusedLocationProviderClient

    val viewModel: RepresentativeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentRepresentativeBinding.inflate(layoutInflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.representativeContainer.setTransition(R.id.start, R.id.start)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.representativeRecycler.adapter =
                RepresentativeListAdapter(RepresentativeListAdapter.RepresentativeListener {})

        viewModel.representatives.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.representativeContainer.setTransition(R.id.start, R.id.start)
            } else {
                binding.representativeContainer.setTransition(R.id.start, R.id.end)
            }
        }
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setState(requireContext().resources.getStringArray(R.array.states)[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //TODO: Establish bindings

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            permissionUtil.requestPermissions(this)
        }
        binding.executePendingBindings()
        return binding.root
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    private fun getLocation() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val address = geoCodeLocation(it)
                        viewModel.searchForRepresentatives(address)
                    }
                }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onGranted() {
        getLocation()
    }

    override fun onDenied() {
        showSnackbar(getString(R.string.error_location_permission_denied))
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}
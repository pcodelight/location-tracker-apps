package com.pcodelight.quadrant.view.fragment

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.pcodelight.model.LocationData
import com.pcodelight.quadrant.R
import com.pcodelight.quadrant.viewmodel.DashboardViewModel
import com.pcodelight.quadrant.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login_screen.view.*
import kotlinx.android.synthetic.main.section_home.view.*

class HomeSection : Fragment(
    R.layout.section_home
) {
    private val viewModel: DashboardViewModel by activityViewModels()
    private fun getFormattedLocation(location: Location?): String? {
        if (location == null) return null
        return "Your current position, Latitude : ${String.format("%.2f", location.latitude)} " +
                "Longitude: ${String.format("%.2f", location.longitude)}"
    }

    private val locationDataObserver = Observer<Location?> { location ->
        view?.let { view ->
            if (location == null) {
                view.btnSendData.isEnabled = false
                view.tvStatus.text = getString(R.string.no_location)
            } else {
                view.btnSendData.isEnabled = true
                view.tvStatus.text = getFormattedLocation(location)
            }
        }
    }

    private val isLoadingDeviceLocation = Observer<Boolean> { isRetrievingLocation ->
        view?.btnSendData?.isEnabled = isRetrievingLocation.not()
    }

    private val onRequestSuccess = Observer<LocationData> {
        if (it != null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.sent_successfully),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val onRequestError = Observer<String> {
        if (it.isNotBlank()) {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            locationData.observe(requireActivity(), locationDataObserver)
            isGettingDeviceLocation.observe(requireActivity(), isLoadingDeviceLocation)
            sendLocationResult.observe(requireActivity(), onRequestSuccess)
            sendLocationError.observe(requireActivity(), onRequestError)
        }

        view.btnSendData.setOnClickListener {
            viewModel.sendDataToServer()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.initTracking()
    }
}
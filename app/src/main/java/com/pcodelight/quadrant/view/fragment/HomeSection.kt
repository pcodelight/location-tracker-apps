package com.pcodelight.quadrant.view.fragment

import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.pcodelight.quadrant.R
import com.pcodelight.quadrant.viewmodel.DashboardViewModel
import com.pcodelight.quadrant.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login_screen.view.*
import kotlinx.android.synthetic.main.section_home.view.*

class HomeSection : Fragment(
    R.layout.section_home
) {
    private lateinit var viewModel: DashboardViewModel

    private fun getFormattedLocation(location: Location?): String? {
        if (location == null) return null
        return "Lat: ${String.format("%.2f", location.latitude)} " +
                "Lng: ${String.format("%.2f", location.longitude)}"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelFactory.getViewModel(DashboardViewModel::class.java.toString())
                as DashboardViewModel

        viewModel.apply {
            locationData.observe(requireActivity(), Observer { location ->
                if (location == null) {
                    view.btnSendData.isEnabled = false
                    view.tvStatus.text = getString(R.string.no_location)
                } else {
                    view.btnSendData.isEnabled = true
                    view.tvStatus.text = getFormattedLocation(location)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.sent_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            isGettingDeviceLocation.observe(requireActivity(), Observer { isRetrievingLocation ->
                view.btnSendData.isEnabled = isRetrievingLocation.not()
            })
        }

        view.btnSendData.setOnClickListener {
            viewModel.sendDataToServer()
        }
    }
}
package com.pcodelight.quadrant.view.fragment

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pcodelight.quadrant.R
import com.pcodelight.quadrant.viewmodel.DashboardViewModel
import com.pcodelight.quadrant.viewmodel.HomeSectionViewModel
import com.pcodelight.quadrant.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.section_home.*
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
            locationData.observe(requireActivity(), Observer {
                view.btnSendData.isEnabled = it != null
                view.tvStatus.text = getFormattedLocation(it) ?: "No location available"
            })

            isPermissionDenied.observe(requireActivity(), Observer {
                view.tvStatus.text = getString(R.string.need_permission)
            })
        }

        view.btnSendData.setOnClickListener {
            viewModel.sendDataToServer()
        }
    }
}
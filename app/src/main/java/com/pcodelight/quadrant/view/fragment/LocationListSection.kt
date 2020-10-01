package com.pcodelight.quadrant.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.pcodelight.model.LocationData
import com.pcodelight.quadrant.R
import com.pcodelight.quadrant.view.ui.LocationListItem
import com.pcodelight.quadrant.view.ui.TextItem
import com.pcodelight.quadrant.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.section_list_data.*

class LocationListSection : Fragment(
    R.layout.section_list_data
) {
    private val itemAdapter = ItemAdapter<AbstractItem<*>>()
    private val adapter = FastAdapter.with(itemAdapter)
    private val viewModel: DashboardViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.apply {
            isListLocationDataLoading.observe(requireActivity(), isLoading)
            locationRequestResult.observe(requireActivity(), locations)
            locationRequestError.observe(requireActivity(), error)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.getLocations()
    }

    private fun initView() {
        rvLocations.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        rvLocations.adapter = adapter
        swipeLayout.setOnRefreshListener {
            viewModel.getLocations()
        }
    }

    private val isLoading = Observer<Boolean> {
        if (it) {
            lvParent.visibility = View.VISIBLE
            rvLocations.visibility = View.GONE
        } else {
            lvParent.visibility = View.GONE
            rvLocations.visibility = View.VISIBLE
            swipeLayout.isRefreshing = false
        }
    }

    private val locations = Observer<List<LocationData>> {
        lvParent.visibility = View.GONE
        rvLocations.visibility = View.VISIBLE

        if (it.isNotEmpty()) {
            itemAdapter.set(it.map { locData ->
                LocationListItem(locData)
            })
        } else {
            itemAdapter.set(listOf(TextItem("No history available...")))
        }
    }

    private val error = Observer<String> {
        lvParent.visibility = View.GONE

        if (it.isNotBlank()) {
            itemAdapter.set(listOf(TextItem(it)))
        }
    }
}
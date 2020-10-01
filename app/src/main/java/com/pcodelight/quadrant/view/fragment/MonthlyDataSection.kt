package com.pcodelight.quadrant.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.pcodelight.model.MonthlyData
import com.pcodelight.quadrant.R
import com.pcodelight.quadrant.viewmodel.DashboardViewModel
import com.pcodelight.quadrant.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.section_insight.view.*


class MonthlyDataSection : Fragment(
    R.layout.section_insight
) {
    private val monthName = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "November",
        "December"
    )

    private lateinit var viewModel: DashboardViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelFactory.getViewModel(DashboardViewModel::class.java.toString())
                as DashboardViewModel
        viewModel.apply {
            monthlyDataRequestResult.observe(requireActivity(), monthlyData)
            monthlyDataRequestError.observe(requireActivity(), error)
            isMonthlyDataLoading.observe(requireActivity(), isLoading)

            getMonthlyData()
        }
    }

    private val isLoading = Observer<Boolean> {
        if (it) {

        } else {

        }
    }

    private val monthlyData = Observer<List<MonthlyData>> { monthlyData ->
        view?.chartView?.let {
            val lineDataSet = LineDataSet(
                monthlyData.map { mData ->
                    Entry(mData.month.toFloat(), mData.count.toFloat())
                }, "Hit Count"
            ).apply {
                color = ContextCompat.getColor(it.context, R.color.materialBlue)
            }

            it.data = LineData(lineDataSet)
            it.invalidate()
        }
    }

    private val error = Observer<String> {
        if (it.isNotBlank()) {

        } else {

        }
    }
}
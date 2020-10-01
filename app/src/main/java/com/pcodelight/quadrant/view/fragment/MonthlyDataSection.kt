package com.pcodelight.quadrant.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.pcodelight.model.MonthlyData
import com.pcodelight.quadrant.R
import com.pcodelight.quadrant.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.section_insight.*
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
        "October",
        "November",
        "December"
    )
    private val viewModel: DashboardViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.apply {
            monthlyDataRequestResult.observe(requireActivity(), monthlyData)
            monthlyDataRequestError.observe(requireActivity(), error)
            isMonthlyDataLoading.observe(requireActivity(), isLoading)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.getMonthlyData()
    }

    private fun initView() {
        swipeLayoutChart.setOnRefreshListener {
            viewModel.getMonthlyData()
        }
    }

    private val isLoading = Observer<Boolean> {
        view?.apply {
            if (it) {
                lvChart.visibility = View.VISIBLE
                tvTitle.visibility = View.GONE
                chartView.visibility = View.GONE
            } else {
                lvChart.visibility = View.GONE
                tvTitle.visibility = View.VISIBLE
                chartView.visibility = View.VISIBLE
                swipeLayoutChart.isRefreshing = false
            }
        }
    }

    private val xAxisFormatter = object: ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return monthName[value.toInt()-1]
        }
    }

    private val monthlyData = Observer<List<MonthlyData>> { monthlyData ->
        view?.chartView?.apply {
            val lineDataSet = LineDataSet(
                monthlyData.map { mData ->
                    Entry(mData.month.toFloat(), mData.count.toFloat())
                }, "Hit Count"
            ).apply {
                color = ContextCompat.getColor(context, R.color.materialBlue)
            }

            legend.isEnabled = false
            description = null
            xAxis.apply {
                granularity = 1f
                valueFormatter = xAxisFormatter
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
            }
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)
            setVisibleXRangeMinimum(1f)
            setVisibleXRangeMaximum(12f)
            data = LineData(lineDataSet)
            invalidate()
        }
    }

    private val error = Observer<String> {
        if (it.isNotBlank()) {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
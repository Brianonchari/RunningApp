package co.studycode.runningapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import co.studycode.runningapp.R
import co.studycode.runningapp.ui.viewmodels.StatisticsViewModel
import co.studycode.runningapp.utils.CustomMarkerView
import co.studycode.runningapp.utils.TrackingUtility
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import java.lang.Math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun setupBarChart() {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = R.color.orange
            textColor = R.color.orange
            setDrawGridLines(false)
        }

        barChart.axisLeft.apply {
            axisLineColor = R.color.orange
            textColor =R.color.orange
            setDrawGridLines(false)

            barChart.axisRight.apply {
                axisLineColor = R.color.orange
                textColor = R.color.orange
                setDrawGridLines(false)
            }
        }
        barChart.apply {
            description.text = "Average Speed over time"
            legend.isEnabled = false
        }

        barChart.setPinchZoom(true)
        barChart.animateY(1000)
        barChart.axisRight.setDrawLabels(false)
    }

    private fun subscribeToObservers() {
        viewModel.totalRunTime.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalRunTime = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = totalRunTime
            }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f
                val totalDistanceString = "${totalDistance}Km"
                tvTotalDistance.text = totalDistanceString
            }
        })

        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer {
            if(it!=null) {
                val avgSpeed = round(it * 10f) / 10f
                val avgSpeedString = "${avgSpeed}km/h"
                tvAverageSpeed.text = avgSpeedString
            }else{
                Toast.makeText(requireContext(), "No data ", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.totalKclBurned.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalKcl = "${it}kcal"
                tvTotalCalories.text = totalKcl
            }
        })

        viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer {
            it?.let {
                val allAvgSpeeds = it.indices.map { i -> BarEntry(i.toFloat(), it[i].avgSpeedInKMH) }
                val barDataSet = BarDataSet(allAvgSpeeds," Avg Speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.barchart_color)
                }
                barChart.data = BarData(barDataSet)
                barChart.data.barWidth =0.2f
                barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                barChart.invalidate()
            }
        })

    }
}
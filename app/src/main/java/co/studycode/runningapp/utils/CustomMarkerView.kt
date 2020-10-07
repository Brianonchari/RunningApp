package co.studycode.runningapp.utils

import android.content.Context
import co.studycode.runningapp.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val runs: List<Run>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width/2f, -height.toFloat())
    }
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if(e==null){
            return
        }
        val curRunId = e.x.toInt()
        var run = runs[curRunId]
        val calender = Calendar.getInstance().apply {
            timeInMillis = run.timeStamp
        }

        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        val date = dateFormat.format(calender.time)
        val _date = "Date :$date"
        tvDate.text = _date

        val avgSpeed = "Speed :${run.avgSpeedInKMH}km/h"
        tvAvgSpeed.text = avgSpeed

        val distanceInKm = "Distance : ${run.distanceInMeters / 1000f}Km"
        tvDistance.text = distanceInKm

        val timeFormart = TrackingUtility.getFormattedStopWatchTime(run.timeInMilis)
        val time = "Time : $timeFormart"
        tvDuration.text = time

        val calloriesBurned = "Calories : ${run.caloriesBurned}kcal"
        tvCaloriesBurned.text = calloriesBurned
    }
}
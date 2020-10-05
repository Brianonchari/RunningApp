package co.studycode.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import co.studycode.runningapp.R
import co.studycode.runningapp.utils.TrackingUtility
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    val args: DetailFragmentArgs by navArgs()
    companion object{
        private const val TAG = "DetailFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val run = args.run
        val runImg= run?.img
        val distance= run?.distanceInMeters
        val distanceInKm = (distance?.div(1000f))
        val _timeTaken = run?.timeInMilis
        val _kcalBurned = run?.caloriesBurned
        val _avgSpeed = run?.avgSpeedInKMH
        val totalRunTime = _timeTaken?.let { TrackingUtility.getFormattedStopWatchTime(it) }

        Glide.with(this).load(runImg).into(detailImg)
        distanceCoverd.text = "${distanceInKm}Km"
        timeTaken.text = "$totalRunTime"
        kcalBurned.text = "${_kcalBurned}kcal"
        avgSpeed.text = "${_avgSpeed}km/h   "
    }
}
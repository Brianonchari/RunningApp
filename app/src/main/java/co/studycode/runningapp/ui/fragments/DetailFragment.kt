package co.studycode.runningapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import co.studycode.runningapp.R
import co.studycode.runningapp.ui.viewmodels.MainViewModel
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
        val _timeTaken = run?.timeInMilis
        val _kcalBurned = run?.caloriesBurned
        val _avgSpeed = run?.avgSpeedInKMH

        Glide.with(this).load(runImg).into(detailImg)
        distanceCoverd.text = "$distance"
        timeTaken.text = "$_timeTaken"
        kcalBurned.text = "$_kcalBurned"
        avgSpeed.text = "$_avgSpeed"

    }
}
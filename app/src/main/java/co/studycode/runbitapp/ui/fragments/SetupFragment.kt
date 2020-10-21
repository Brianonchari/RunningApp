package co.studycode.runbitapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import co.studycode.runbitapp.R
import co.studycode.runbitapp.utils.Constants.KET_FIRST_TIME_TOGLE
import co.studycode.runbitapp.utils.Constants.KEY_NAME
import co.studycode.runbitapp.utils.Constants.KEY_WEIGHT
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setup.*
import kotlinx.android.synthetic.main.fragment_setup.adview
import javax.inject.Inject


@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {
    @Inject
    lateinit var sharedPref: SharedPreferences
    @set:Inject
    var isFirstTime = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        adview.loadAd(adRequest)
        if (!isFirstTime) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }
        save.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "Please enter all fields", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = nameEt.text.toString()
        val weight = weightEt.text.toString().toFloat()
        if (name.isEmpty() || weight.equals("")) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight)
            .putBoolean(KET_FIRST_TIME_TOGLE, false)
            .apply()
        return true
    }




}
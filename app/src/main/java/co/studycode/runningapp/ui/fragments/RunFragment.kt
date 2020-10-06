package co.studycode.runningapp.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.studycode.runningapp.R
import co.studycode.runningapp.adapters.RunAdapter
import co.studycode.runningapp.ui.viewmodels.MainViewModel
import co.studycode.runningapp.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import co.studycode.runningapp.utils.SortType
import co.studycode.runningapp.utils.TrackingUtility
import com.google.android.gms.ads.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber


@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) , EasyPermissions.PermissionCallbacks{
    private lateinit var mInterstitialAd: InterstitialAd
    private val viewModel: MainViewModel by viewModels()
    private lateinit var runAdapter: RunAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInterstitialAd = InterstitialAd(requireContext())
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        MobileAds.initialize(requireContext())
//        MobileAds.setRequestConfiguration(
//            RequestConfiguration.Builder()
//                .setTestDeviceIds(listOf(""))
//                .build()
//        )
        requestPermissions()
        setupRecyclerView()
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_options, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spFilter.adapter = adapter

        runAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
            putSerializable("run", it)
            }
            findNavController().navigate(R.id.detailFragment, bundle)
        }
        when(viewModel.sortType){
            SortType.DATE -> spFilter.setSelection(0)
            SortType.RUNNING_TIME -> spFilter.setSelection(1)
            SortType.DISTANCE -> spFilter.setSelection(2)
            SortType.AVG_SPEED -> spFilter.setSelection(3)
            SortType.CALLORIES_BURNED -> spFilter.setSelection(4)
        }
        spFilter.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0-> viewModel.sortRuns(SortType.DATE)
                    1-> viewModel.sortRuns(SortType.RUNNING_TIME)
                    2-> viewModel.sortRuns(SortType.DISTANCE)
                    3-> viewModel.sortRuns(SortType.AVG_SPEED)
                    4-> viewModel.sortRuns(SortType.CALLORIES_BURNED)
                }
            }
        }
        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })
        fab.setOnClickListener {
            if(mInterstitialAd.isLoaded){
                mInterstitialAd.show()
            }else{
                Timber.d( "onViewCreated: j")
                findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
            }
        }

        runAdEvents()

    }

    private fun setupRecyclerView() = rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())

    }
    //Request Permissions
    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermisions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) { }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults, this)
    }

    private fun runAdEvents(){
        mInterstitialAd.adListener = object : AdListener(){
            override fun onAdClicked() {
                super.onAdClicked()
                mInterstitialAd.adListener.onAdClosed()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
            }
        }
    }

}
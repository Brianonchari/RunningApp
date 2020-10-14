package co.studycode.runningapp.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.studycode.runningapp.R
import co.studycode.runningapp.adapters.RunAdapter
import co.studycode.runningapp.ui.viewmodels.MainViewModel
import co.studycode.runningapp.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import co.studycode.runningapp.utils.SortType
import co.studycode.runningapp.utils.TrackingUtility
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber


@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var runAdapter: RunAdapter
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        setupRecyclerView()
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_options, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spFilter.adapter = adapter
        //Interstitial ads
        MobileAds.initialize(requireContext()) {}
        mInterstitialAd = InterstitialAd(requireContext())
        mInterstitialAd.adUnitId = "ca-app-pub-7628201468416367/2150313203"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        runAdEvents()

        runAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("run", it)
            }
            findNavController().navigate(R.id.detailFragment, bundle)
        }
        when (viewModel.sortType) {
            SortType.DATE -> spFilter.setSelection(0)
            SortType.RUNNING_TIME -> spFilter.setSelection(1)
            SortType.DISTANCE -> spFilter.setSelection(2)
            SortType.AVG_SPEED -> spFilter.setSelection(3)
            SortType.CALLORIES_BURNED -> spFilter.setSelection(4)
        }
        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> viewModel.sortRuns(SortType.DATE)
                    1 -> viewModel.sortRuns(SortType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(SortType.DISTANCE)
                    3 -> viewModel.sortRuns(SortType.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortType.CALLORIES_BURNED)
                }
            }
        }
        viewModel.runs.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                no_contentTv.visibility = View.VISIBLE
            } else {
                runAdapter.submitList(it)
                no_contentTv.visibility = View.INVISIBLE
            }

        })
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Timber.d("The interstitial wasn't loaded yet.")
            }
        }

        val itemTouchHelperobject = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val run = runAdapter.differ.currentList[position]
                viewModel.deleteRun(run)
                Snackbar.make(view, "Item Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        viewModel.insertRun(run)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperobject).apply {
            attachToRecyclerView(rvRuns)
        }


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
                "This  app needs location permission in order to function properly and track your runs effectively.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This  app needs location permission in order to function properly and track your runs effectively.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    private fun runAdEvents() {
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdClicked() {
                super.onAdClicked()
                findNavController().navigate(R.id.trackingFragment)
            }
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                Timber.d("Ad failed to load ${p0?.message}")
            }
        }
    }
}
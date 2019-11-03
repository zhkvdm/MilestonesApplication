package com.example.milestonesapplication.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.milestonesapplication.R
import com.example.milestonesapplication.ViewModel
import com.example.milestonesapplication.adapters.MarkerInfoViewAdapter
import com.example.milestonesapplication.adapters.SpinnerAdapter
import com.example.milestonesapplication.databinding.ActivityMainBinding
import com.example.milestonesapplication.interfaces.FailureDialogInterface
import com.example.milestonesapplication.interfaces.MainViewInterface
import com.example.milestonesapplication.models.Milestone
import com.example.milestonesapplication.models.Region
import com.example.milestonesapplication.views.FailureDialog
import com.example.milestonesapplication.views.InfoWindow
import com.example.milestonesapplication.views.MilestoneMarker
import com.example.milestonesapplication.views.WaitDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import java.util.*


class MainView : AppCompatActivity(), MainViewInterface, OnMapReadyCallback, FailureDialogInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private var clusterManager: ClusterManager<MilestoneMarker>? = null
    private var waitDialog: WaitDialog? = null
    private var failureDialog: FailureDialog? = null

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModel(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            parentView = this@MainView
            refreshButton.setOnClickListener { viewModel.onRefresh(binding.spinner.selectedItemPosition) }
        }

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.onCreate()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun setupSpinner(regions: ArrayList<Region>) {
        if (regions.size > 0) {
            val arrayAdapter = SpinnerAdapter(this, 0, regions)

            binding.spinner.apply {
                adapter = arrayAdapter
                setSelection(0)

                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, l: Long) {
                        viewModel.spinnerItemSelected(position)
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {}
                }
            }
        }
    }

    override fun onRefreshClick() {
        viewModel.onRefresh(binding.spinner.selectedItemPosition)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        setMap()
    }

    override fun setupMap(milestones: ArrayList<Milestone>) {
        clusterManager?.clearItems()
        val builder = LatLngBounds.builder()
        for (milestone in milestones) {
            clusterManager?.addItem(MilestoneMarker(milestone))
            builder.include(milestone.location)
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
        binding.refreshButton.visibility = View.VISIBLE
    }

    override fun showWaitDialog() {
        waitDialog = WaitDialog()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        waitDialog?.show(fragmentTransaction, WaitDialog.TAG)
    }

    override fun dismissWaitDialog() {
        if (waitDialog!!.isAdded)
            waitDialog?.dismiss()
    }

    override fun showFailureDialog() {
        failureDialog = FailureDialog()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        failureDialog?.show(fragmentTransaction, FailureDialog.TAG)
    }

    override fun dismissFailureDialog() {
        if (failureDialog!!.isAdded)
            failureDialog?.dismiss()
    }

    private fun setMap() {
        clusterManager = ClusterManager(this, googleMap)
        clusterManager?.apply {
            setOnClusterClickListener { false }

            markerCollection?.setOnInfoWindowAdapter(
                    MarkerInfoViewAdapter(LayoutInflater.from(applicationContext)))

            setOnClusterItemInfoWindowClickListener { milestoneMarker ->
                val infoWindow = InfoWindow.newInstance(
                        milestoneMarker.title!!,
                        milestoneMarker.description!!,
                        milestoneMarker.position!!)
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                infoWindow.show(fragmentTransaction, InfoWindow.TAG)
            }
        }

        googleMap?.apply {
            setInfoWindowAdapter(clusterManager!!.markerManager)
            setOnCameraIdleListener(clusterManager)
            setOnMarkerClickListener(clusterManager)
            setOnInfoWindowClickListener(clusterManager)
            uiSettings.apply {
                isZoomControlsEnabled = true
                isRotateGesturesEnabled = false
                isMapToolbarEnabled = false
            }
        }
    }
}

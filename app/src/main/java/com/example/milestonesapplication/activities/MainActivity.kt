package com.example.milestonesapplication.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.milestonesapplication.R
import com.example.milestonesapplication.adapters.MarkerInfoViewAdapter
import com.example.milestonesapplication.adapters.SpinnerAdapter
import com.example.milestonesapplication.databinding.ActivityMainBinding
import com.example.milestonesapplication.interfaces.FailureDialogInterface
import com.example.milestonesapplication.models.Milestone
import com.example.milestonesapplication.models.Region
import com.example.milestonesapplication.utils.HttpProvider
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


class MainActivity : AppCompatActivity(), HttpProvider.HttpProviderInterface, OnMapReadyCallback, FailureDialogInterface {

    private var googleMap: GoogleMap? = null
    private var clusterManager: ClusterManager<MilestoneMarker>? = null

    lateinit var binding: ActivityMainBinding

    private var mapFragment: SupportMapFragment? = null
    private lateinit var builder: LatLngBounds.Builder
    private var regions = ArrayList<Region>()
    private var milestones = ArrayList<Milestone>()
    private var waitDialog: WaitDialog? = null
    private var failureDialog: FailureDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.parentView = this

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                ?: return
        mapFragment?.getMapAsync(this)

        builder = LatLngBounds.builder()

        getData("all")
    }

    override fun onTaskPostExecute(result: Int) {
        dismissWaitDialog()
        showFailureDialog()
    }

    override fun onTaskPostExecute(regions: ArrayList<Region>, milestones: ArrayList<Milestone>) {
        if (regions.size == 0) {
            dismissWaitDialog()
            dismissFailureDialog()
            return
        } else if (this.regions.size == 0) {
            this.regions = regions
            setSpinner(this.regions)
            binding.refreshButton.visibility = View.VISIBLE
        }

        if (milestones.size == 0) {
            dismissWaitDialog()
            return
        }
        this.milestones = milestones
        clusterManager?.clearItems()
        builder = LatLngBounds.builder()
        for (milestone in milestones) {
            outMilestoneToMap(milestone)
        }

        clusterManager?.cluster()

        fitMapPoints(builder)

        dismissWaitDialog()
    }

    private fun setSpinner(regions: ArrayList<Region>) {
        if (regions.size > 0) {
            val arrayAdapter = SpinnerAdapter(this, 0, regions)

            binding.spinner.adapter = arrayAdapter
            binding.spinner.setSelection(0)

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, l: Long) {
                    if (position != 0) {
                        getData(regions[position].code)
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
        }
    }

    override fun refresh() {
        getData(if (binding.spinner.adapter != null)
            regions[binding.spinner.selectedItemPosition].code
        else
            "all")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        setGoogleMap()
    }

    private fun setGoogleMap() {
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
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isRotateGesturesEnabled = false
            uiSettings.isMapToolbarEnabled = false
        }
    }

    private fun fitMapPoints(builder: LatLngBounds.Builder) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
    }

    internal fun getData(regionCode: String?) {
        showWaitDialog()
        val task = HttpProvider(regionCode)
        task.setDelegate(this)
        task.execute()
    }

    private fun outMilestoneToMap(milestone: Milestone) {
        if (googleMap != null) {
            clusterManager?.addItem(MilestoneMarker(milestone))
            builder.include(milestone.location)
        }
    }

    private fun showWaitDialog() {
        waitDialog = WaitDialog()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        waitDialog?.show(fragmentTransaction, WaitDialog.TAG)
    }

    private fun dismissWaitDialog() {
        if (waitDialog!!.isAdded)
            waitDialog?.dismiss()
    }

    private fun showFailureDialog() {
        failureDialog = FailureDialog()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        failureDialog?.show(fragmentTransaction, FailureDialog.TAG)
    }

    private fun dismissFailureDialog() {
        if (failureDialog!!.isAdded)
            failureDialog?.dismiss()
    }
}

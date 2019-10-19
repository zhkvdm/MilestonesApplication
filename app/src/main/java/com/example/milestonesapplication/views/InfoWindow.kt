package com.example.milestonesapplication.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.milestonesapplication.R
import com.example.milestonesapplication.databinding.WindowInfoLayoutBinding
import com.example.milestonesapplication.models.Milestone
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val ARG_NAME = "name"
private const val ARG_DESCRIPTION = "description"
private const val ARG_LOCATION = "location"

class InfoWindow : DialogFragment(), OnMapReadyCallback {

    private lateinit var binding: WindowInfoLayoutBinding
    private lateinit var googleMap: GoogleMap

    private var name: String? = null
    private var description: String? = null
    private var location: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_NAME)
            description = it.getString(ARG_DESCRIPTION)
            location = it.getParcelable(ARG_LOCATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = WindowInfoLayoutBinding.inflate(inflater, container, false)
        binding.milestone = Milestone(name!!, description!!, location!!)
        binding.parentView = this

        val mapFragment: SupportMapFragment =
                fragmentManager?.findFragmentById(R.id.map_info_window) as SupportMapFragment?
                ?: return null
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val f = fragmentManager?.findFragmentById(R.id.map_info_window)
        fragmentManager?.beginTransaction()?.remove(f!!)?.commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        setGoogleMap()
    }

    private fun setGoogleMap() {
        googleMap.apply {
            addMarker(MarkerOptions().position(location!!))
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isZoomGesturesEnabled = false
            uiSettings.isScrollGesturesEnabled = false
            uiSettings.isRotateGesturesEnabled = false
            uiSettings.isMapToolbarEnabled = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, description: String, location: LatLng): InfoWindow {
            val fragment = InfoWindow()
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_DESCRIPTION, description)
            args.putParcelable(ARG_LOCATION, location)
            fragment.arguments = args
            return fragment
        }

        @JvmField
        val TAG = InfoWindow::class.java.name
    }
}

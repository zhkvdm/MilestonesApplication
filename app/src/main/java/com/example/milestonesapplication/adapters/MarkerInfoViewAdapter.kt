package com.example.milestonesapplication.adapters

import android.view.LayoutInflater
import android.view.View

import androidx.databinding.DataBindingUtil

import com.example.milestonesapplication.R
import com.example.milestonesapplication.databinding.MarkerInfoLayoutBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoViewAdapter(private val layoutInflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val binding = MarkerInfoLayoutBinding.inflate(layoutInflater)
        binding.title.text = marker.title
        binding.executePendingBindings()
        return binding.root
    }
}

package com.example.milestonesapplication.views

import com.example.milestonesapplication.models.Milestone
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MilestoneMarker(private val milestone: Milestone) : ClusterItem {

    val description: String?
        get() = milestone.name

    override fun getPosition(): LatLng? {
        return milestone.location
    }

    override fun getTitle(): String? {
        return milestone.description
    }

    override fun getSnippet(): String? {
        return null
    }
}

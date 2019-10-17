package com.example.milestonesapplication.views;

import android.text.TextUtils;

import com.example.milestonesapplication.model.Milestone;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MilestoneMarker implements ClusterItem {

    private final Milestone milestone;

    public MilestoneMarker(Milestone milestone) {
        this.milestone = milestone;
    }

    @Override
    public LatLng getPosition() {
        return milestone.getLocation();
    }

    @Override
    public String getTitle() {
        return milestone.getDescription();
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getDescription() {
        return milestone.getName();

    }
}

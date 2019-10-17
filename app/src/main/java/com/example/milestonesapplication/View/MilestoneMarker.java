package com.example.milestonesapplication.View;

import android.text.TextUtils;

import com.example.milestonesapplication.Model.Milestone;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MilestoneMarker implements ClusterItem {

    private final Milestone milestone;

    public MilestoneMarker(Milestone milestone) {
        this.milestone = milestone;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(
                milestone.getLocation().getLatitude(),
                milestone.getLocation().getLongitude());
    }

    @Override
    public String getTitle() {
        return milestone.getHintContent();
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getDescription() {
        String result = "";
        if (milestone.getBalloonContentBody().size() > 0) {
            String[] stringArray = new String[milestone.getBalloonContentBody().size()];
            stringArray = milestone.getBalloonContentBody().toArray(stringArray);
            result = TextUtils.join(";\n", stringArray);
        }
        return result;

    }
}

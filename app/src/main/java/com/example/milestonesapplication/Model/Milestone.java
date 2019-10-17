package com.example.milestonesapplication.model;

import com.google.android.gms.maps.model.LatLng;

public class Milestone {
    private String name;
    private String description;
    private LatLng location;

    public Milestone() {
    }

    public Milestone(String balloonContentBody, String description, LatLng location) {
        this.name = balloonContentBody;
        this.description = description;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getLocationString() {
        return location.latitude + " " + location.longitude;
    }

    public void setLocationString(String locationString) {
    }
}

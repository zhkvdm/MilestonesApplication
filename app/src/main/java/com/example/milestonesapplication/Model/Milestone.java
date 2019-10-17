package com.example.milestonesapplication.Model;

import android.location.Location;

import java.util.ArrayList;

public class Milestone {
    private ArrayList<String> balloonContentBody;
    private String hintContent;
    private String balloonContentHeader;
    private String type;
    private Location location;

    public Milestone() {
    }

    public Milestone(ArrayList<String> balloonContentBody, String hintContent, String balloonContentHeader, String type, Location location) {
        this.balloonContentBody = balloonContentBody;
        this.hintContent = hintContent;
        this.balloonContentHeader = balloonContentHeader;
        this.type = type;
        this.location = location;
    }

    public ArrayList<String> getBalloonContentBody() {
        return balloonContentBody;
    }

    public void setBalloonContentBody(ArrayList<String> balloonContentBody) {
        this.balloonContentBody = balloonContentBody;
    }

    public String getHintContent() {
        return hintContent;
    }

    public void setHintContent(String hintContent) {
        this.hintContent = hintContent;
    }

    public String getBalloonContentHeader() {
        return balloonContentHeader;
    }

    public void setBalloonContentHeader(String balloonContentHeader) {
        this.balloonContentHeader = balloonContentHeader;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

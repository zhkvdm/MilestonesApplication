package com.example.milestonesapplication.Model;

public class Region {
    private String code;
    private String region;

    public Region() {
    }

    public Region(String code, String region) {
        this.code = code;
        this.region = region;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}

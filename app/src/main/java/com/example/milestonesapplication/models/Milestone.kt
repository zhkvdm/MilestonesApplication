package com.example.milestonesapplication.models

import com.google.android.gms.maps.model.LatLng

class Milestone {
    var name: String? = null
    var description: String? = null
    var location: LatLng? = null

    var locationString: String
        get() = location?.latitude.toString() + " " + location?.longitude
        set(locationString) {}

    constructor() {}

    constructor(balloonContentBody: String, description: String, location: LatLng) {
        this.name = balloonContentBody
        this.description = description
        this.location = location
    }
}

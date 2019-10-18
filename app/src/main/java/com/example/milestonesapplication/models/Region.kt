package com.example.milestonesapplication.models

class Region {
    var code: String? = null
    var region: String? = null

    constructor() {}

    constructor(code: String, region: String) {
        this.code = code
        this.region = region
    }
}

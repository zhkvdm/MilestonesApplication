package com.example.milestonesapplication.interfaces

import com.example.milestonesapplication.models.Milestone
import com.example.milestonesapplication.models.Region

interface DataLoadInterface {
    fun onErrorDataLoad()

    fun onDataLoaded(regions: ArrayList<Region>, milestones: ArrayList<Milestone>)
}
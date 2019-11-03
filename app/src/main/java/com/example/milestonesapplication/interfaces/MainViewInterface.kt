package com.example.milestonesapplication.interfaces

import com.example.milestonesapplication.models.Milestone
import com.example.milestonesapplication.models.Region

interface MainViewInterface {
    fun setupSpinner(regions: ArrayList<Region>) {}

    fun setupMap(milestones: ArrayList<Milestone>) {}

    fun showWaitDialog() {}

    fun dismissWaitDialog() {}

    fun showFailureDialog() {}

    fun dismissFailureDialog() {}
}
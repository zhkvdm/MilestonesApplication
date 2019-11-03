package com.example.milestonesapplication

import com.example.milestonesapplication.interfaces.DataLoadInterface
import com.example.milestonesapplication.interfaces.MainViewInterface
import com.example.milestonesapplication.interfaces.ViewModelInterface
import com.example.milestonesapplication.models.Milestone
import com.example.milestonesapplication.models.Region
import com.example.milestonesapplication.utils.HttpProvider
import java.util.*

class ViewModel(private val view: MainViewInterface) : ViewModelInterface, DataLoadInterface {

    private var regions = ArrayList<Region>()
    private var milestones = ArrayList<Milestone>()

    override fun onCreate() {
        getData("all")
    }

    override fun onResume() {
        if(milestones.isNotEmpty())
            view.setupMap(milestones)
    }

    override fun spinnerItemSelected(selectedPosition: Int) {
        if (selectedPosition != 0) {
            getData(regions[selectedPosition].code)
        }
    }

    override fun onRefresh(selectedPosition: Int) {
        getData(if (selectedPosition > 0)
            regions[selectedPosition].code
        else
            "all")
    }

    private fun getData(regionCode: String?) {
        view.showWaitDialog()
        regionCode?.let {
            val task = HttpProvider(this)
            task.setRegion(it)
            task.execute()
        }
    }

    override fun onErrorDataLoad() {
        view.dismissWaitDialog()
        view.showFailureDialog()
    }

    override fun onDataLoaded(regions: ArrayList<Region>, milestones: ArrayList<Milestone>) {
        if (this.regions.size == 0) {
            this.regions = regions
            view.setupSpinner(this.regions)
        }

        if (milestones.size == 0) {
            view.dismissWaitDialog()
            return
        }
        this.milestones = milestones
        view.setupMap(milestones)

        view.dismissWaitDialog()
    }
}
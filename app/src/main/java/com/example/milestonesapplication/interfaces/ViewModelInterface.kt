package com.example.milestonesapplication.interfaces

interface ViewModelInterface {
    fun onCreate()

    fun onResume()

    fun spinnerItemSelected(selectedPosition: Int)

    fun onRefresh(selectedPosition: Int)
}
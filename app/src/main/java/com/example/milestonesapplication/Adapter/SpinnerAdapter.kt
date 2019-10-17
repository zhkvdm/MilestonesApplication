package com.example.milestonesapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.milestonesapplication.Model.Region
import com.example.milestonesapplication.R
import kotlinx.android.synthetic.main.spinner_row_layout.view.*


class SpinnerAdapter(context: Context, resource: Int, objects: MutableList<Region>) : ArrayAdapter<Region>(context, resource, objects) {

    var regions: MutableList<Region> = objects
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {

        val row = layoutInflater.inflate(R.layout.spinner_row_layout, parent, false)
        row.text.text = regions[position].region

        return row
    }
}
package com.example.milestonesapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.milestonesapplication.R;
import com.example.milestonesapplication.databinding.MarkerInfoLayoutBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoViewAdapter implements GoogleMap.InfoWindowAdapter {

    private final LayoutInflater layoutInflater;

    public MarkerInfoViewAdapter(LayoutInflater inflater) {
        this.layoutInflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        MarkerInfoLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(layoutInflater.getContext()), R.layout.marker_info_layout, null, true);
        binding.title.setText(marker.getTitle());
        binding.description.setText(marker.getSnippet());
        binding.executePendingBindings();
        return binding.getRoot();
    }
}

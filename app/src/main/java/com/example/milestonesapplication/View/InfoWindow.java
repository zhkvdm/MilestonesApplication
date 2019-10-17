package com.example.milestonesapplication.View;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.milestonesapplication.R;
import com.example.milestonesapplication.databinding.WindowInfoLayoutBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InfoWindow extends DialogFragment implements OnMapReadyCallback {

    public static final String TAG = InfoWindow.class.getName();

    private WindowInfoLayoutBinding binding;
    private GoogleMap googleMap;

    private String name;
    private String description;
    private LatLng location;

    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_LOCATION = "location";

    public static InfoWindow newInstance(String title,
                                         String description,
                                         LatLng location) {
        InfoWindow fragment = new InfoWindow();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putParcelable(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            name = args.getString(ARG_NAME);
            description = args.getString(ARG_DESCRIPTION);
            location = args.getParcelable(ARG_LOCATION);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_info_layout, container, false);
        binding.name.setText(name);
        binding.description.setText(description);
        String locationString = location.latitude + " " + location.longitude;
        binding.location.setText(locationString);
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        SupportMapFragment mapFragment = null;
        if (getFragmentManager() != null) {
            mapFragment = (SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map_info_window);
        }
        if (mapFragment == null) {
            return null;
        }
        mapFragment.getMapAsync(this);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(width, height);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment f = null;
        if (getFragmentManager() != null) {
            f = getFragmentManager().findFragmentById(R.id.map_info_window);
        }
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setGoogleMap();
    }

    private void setGoogleMap() {
        googleMap.addMarker(new MarkerOptions()
                .position(location));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(false);
        this.googleMap.getUiSettings().setScrollGesturesEnabled(false);
        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
    }
}

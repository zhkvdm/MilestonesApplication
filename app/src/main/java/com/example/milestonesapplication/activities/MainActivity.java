package com.example.milestonesapplication.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.example.milestonesapplication.R;
import com.example.milestonesapplication.adapters.MarkerInfoViewAdapter;
import com.example.milestonesapplication.adapters.SpinnerAdapter;
import com.example.milestonesapplication.databinding.ActivityMainBinding;
import com.example.milestonesapplication.interfaces.FailureDialogInterface;
import com.example.milestonesapplication.model.Milestone;
import com.example.milestonesapplication.model.Region;
import com.example.milestonesapplication.utils.HttpProvider;
import com.example.milestonesapplication.views.FailureDialog;
import com.example.milestonesapplication.views.InfoWindow;
import com.example.milestonesapplication.views.MilestoneMarker;
import com.example.milestonesapplication.views.WaitDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements
        HttpProvider.HttpProviderInterface,
        OnMapReadyCallback,
        FailureDialogInterface {

    private GoogleMap googleMap;
    private ClusterManager<MilestoneMarker> clusterManager;

    ActivityMainBinding binding;
    SupportMapFragment mapFragment;
    LatLngBounds.Builder builder;
    ArrayList<Region> regions = new ArrayList<>();
    ArrayList<Milestone> milestones;
    WaitDialog waitDialog;
    FailureDialog failureDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            return;
        }

        binding.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.spinner.getSelectedItemPosition() != 0)
                    getData(regions.get(binding.spinner.getSelectedItemPosition()).getCode());
            }
        });

        mapFragment.getMapAsync(this);

        builder = LatLngBounds.builder();

        getData("all");
    }

    @Override
    public void onTaskPostExecute(int result) {
        dismissWaitDialog();
        failureDialog = new FailureDialog();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        failureDialog.show(fragmentTransaction, FailureDialog.TAG);
    }

    @Override
    public void onTaskPostExecute(@NotNull ArrayList<Region> regions, @NotNull ArrayList<Milestone> milestones) {
        if (regions.size() == 0) {
            dismissWaitDialog();
            dismissFailureDialog();
            return;
        } else if (this.regions.size() == 0) {
            this.regions = regions;
            setSpinner(this.regions);
            binding.refreshButton.setVisibility(View.VISIBLE);
        }

        if (milestones.size() == 0) {
            dismissWaitDialog();
            return;
        }
        this.milestones = milestones;
        clusterManager.clearItems();
        builder = LatLngBounds.builder();
        for (Milestone milestone : milestones) {
            outMilestoneToMap(milestone);
        }

        clusterManager.cluster();

        fitMapPoints(builder);

        dismissWaitDialog();
    }

    void setSpinner(final ArrayList<Region> regions) {
        if (regions.size() > 0) {
            SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, 0, regions);

            binding.spinner.setAdapter(arrayAdapter);
            binding.spinner.setSelection(0);

            binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i != 0)
                        getData(regions.get(i).getCode());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    void getData(String regionCode) {
        showWaitDialog();
        HttpProvider task = new HttpProvider(regionCode);
        task.setDelegate(this);
        task.execute();
    }

    private void outMilestoneToMap(Milestone milestone) {
        if (googleMap != null) {
            clusterManager.addItem(new MilestoneMarker(milestone));
            builder.include(milestone.getLocation());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setGoogleMap();
    }

    private void setGoogleMap() {
        clusterManager = new ClusterManager<>(this, googleMap);
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MilestoneMarker>() {
            @Override
            public boolean onClusterClick(Cluster<MilestoneMarker> cluster) {
                return false;
            }
        });

        clusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new MarkerInfoViewAdapter(LayoutInflater.from(this)));

        clusterManager.setOnClusterItemInfoWindowClickListener(
                new ClusterManager.OnClusterItemInfoWindowClickListener<MilestoneMarker>() {
                    @Override
                    public void onClusterItemInfoWindowClick(MilestoneMarker milestoneMarker) {
                        InfoWindow infoWindow = InfoWindow.newInstance(
                                milestoneMarker.getTitle(),
                                milestoneMarker.getDescription(),
                                milestoneMarker.getPosition());
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        infoWindow.show(fragmentTransaction, InfoWindow.TAG);
                    }
                });

        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setOnInfoWindowClickListener(clusterManager);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void fitMapPoints(LatLngBounds.Builder builder) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }

    @Override
    public void refresh() {
        getData(binding.spinner.getAdapter() != null ?
                regions.get(binding.spinner.getSelectedItemPosition()).getCode()
                : "all");
    }

    private void showWaitDialog() {
        waitDialog = new WaitDialog();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        waitDialog.show(fragmentTransaction, WaitDialog.TAG);
    }

    private void dismissWaitDialog() {
        if (waitDialog != null && waitDialog.isAdded())
            waitDialog.dismiss();
    }

    private void dismissFailureDialog() {
        if (failureDialog != null && failureDialog.isAdded())
            failureDialog.dismiss();
    }
}

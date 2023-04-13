package com.example.eventprototype;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment {

    private GoogleMap map;
    private List<Address> listGeoCoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.eventMap);
        try {
            listGeoCoder = new Geocoder(getContext()).getFromLocationName("Sydney", 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        map = googleMap;
                        LatLng sydney = new LatLng(listGeoCoder.get(0).getLatitude(),listGeoCoder.get(0).getLongitude());
                        map.addMarker(new MarkerOptions().position(sydney).title("Sydeny Event"));
                        map.clear();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                        map.getUiSettings().setZoomControlsEnabled(true);

                    }
                });



            }
        });
        return view;
    }
}
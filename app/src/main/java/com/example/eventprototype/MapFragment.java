package com.example.eventprototype;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EventModel;
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
    private List<EventModel> eventList;
    private DatabaseHandler db;
    private DashboardActivity dashboardActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.eventMap);
        //initialise db
        db = new DatabaseHandler(getContext());
        //open database
        db.openDatabase();
        //get a list of all events to be converted into markers
        eventList = db.getAllEvents();

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                for (int i = 0; i < eventList.size(); i++) {
                    try {
                        listGeoCoder = populateGeoCodes(eventList.get(i));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    LatLng location = new LatLng(listGeoCoder.get(0).getLatitude(), listGeoCoder.get(0).getLongitude());
                    map.addMarker(new MarkerOptions().position(location).title(eventList.get(i).getEvent()));
                }
                //map.clear();
                map.getUiSettings().setZoomControlsEnabled(true);

            }
        });
        return view;
    }


    public List<Address> populateGeoCodes(EventModel event) throws IOException {
        List<Address> tempList;
        tempList = new Geocoder(getContext()).getFromLocationName(event.getLocation(), 1);
        return tempList;
    }


}
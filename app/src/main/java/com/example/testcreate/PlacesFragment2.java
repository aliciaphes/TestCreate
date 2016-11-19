package com.example.testcreate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


//@RuntimePermissions
public class PlacesFragment2 extends Fragment {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    //private MapView mMapView;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private FragmentActivity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_places, container, false);
//        if (mapFragment == null) {
//            mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map));
//            // Check if we were successful in obtaining the map.
//            if (mapFragment != null) {
//                buildMap();
//            }
//        }
        return v;
    }

    private void buildMap() {
        if (TextUtils.isEmpty(getResources().getString(R.string.google_maps_api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (googleMap != null) {
                    // Map is ready
                    Toast.makeText(activity, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();

                    map = googleMap;

                    //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    map.getUiSettings().setZoomControlsEnabled(true);

                } else {
                    Toast.makeText(activity, "Error - Map was null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.example.testcreate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;


@RuntimePermissions
public class PlacesFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private int PLACE_PICKER_REQUEST = 1;

    private FragmentManager sfm;
    //private MapView mMapView;
    private GoogleMap map;
    private Place place;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sfm = getActivity().getSupportFragmentManager();

        //set up google places
        mGoogleApiClient = new GoogleApiClient
                .Builder(activity)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        myPlaces();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_places, container, false);
        if (mapFragment == null) {
//            mapFragment = ((SupportMapFragment) sfm.findFragmentById(R.id.map));
            mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        }

        Button b = (Button) v.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPlaces();
            }
        });
        return v;
    }


    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void myPlaces() {
        /**
         If you want to change the place picker's default behavior, you can use the builder to set the initial latitude and longitude bounds of the map displayed by the place picker.
         Call setLatLngBounds() on the builder, passing in a LatLngBounds to set the initial latitude and longitude bounds.
         These bounds define an area called the 'viewport'.
         By default, the viewport is centered on the device's location, with the zoom at city-block level.
         */
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            // Start the Intent by requesting a result, identified by a request code.
            this.startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(activity, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(activity, toastMsg, Toast.LENGTH_LONG).show();

                //draw this on the map:
                if (mapFragment != null) {
                    buildMap();
                }
            }
        }
    }

    private void buildMap() {
        if (TextUtils.isEmpty(getResources().getString(R.string.google_places_api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }


        //mMapView.onResume();// needed to get the map to display immediately

//        try {
//            MapsInitializer.initialize(activity.getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (googleMap != null) {

                    map = googleMap;

//                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                    map.setTrafficEnabled(true);
//                    //map.setIndoorEnabled(true);
//                    map.setBuildingsEnabled(true);
//                    map.getUiSettings().setZoomControlsEnabled(true);

                    centerMapUsingLocation();
                } else {
                    Toast.makeText(activity, "Error - Map was null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void centerMapUsingLocation() {
        LatLng position = place.getLatLng();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map to l
                .zoom(15)                   // Sets the zoom
                //.bearing(90)                // Sets the orientation of the camera to east
                //.tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //put a marker on the place selected:
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(place.getName().toString())
                .snippet(place.getAddress().toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }


//    @SuppressWarnings("all")
//    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
//    void getCurrentLocation() {
//    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(activity, "Error - connection failed", Toast.LENGTH_SHORT).show();
    }
}

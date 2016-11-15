package com.example.testcreate;

import android.Manifest;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class PlacesFragment extends Fragment implements LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private FragmentManager sfm;
    private MapView mMapView;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sfm = activity.getSupportFragmentManager();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_places, container, false);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        buildMap();
        return v;
    }

    private void buildMap() {
        //center the map around current location
        loadMap();

        getCurrentLocation();


        //api call to yelp with current location -- we get back, say, 5 places around me

        //draw these 5 places on the map as pins and make them clickable
    }


    private void loadMap() {
        if (TextUtils.isEmpty(getResources().getString(R.string.google_maps_api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }

//        Fragment fragment = sfm.getFragments().get(0);
//
//        mapFragment = ((SupportMapFragment)sfm.findFragmentById(R.id.map));
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap map) {
//                    drawMap(map);
//                }
//            });
//        } else {
//            Toast.makeText(activity, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
//        }

        //mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (googleMap != null) {

                    map = googleMap;

                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    //map.setMyLocationEnabled(true);
                    map.setTrafficEnabled(true);
                    map.setIndoorEnabled(true);
                    map.setBuildingsEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(true);
                }else {
                    Toast.makeText(activity, "Error - Map was null", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getCurrentLocation() {
        if (map != null) {
            // Now that map has loaded, let's get our location!
            map.setMyLocationEnabled(true);
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    //.addConnectionCallbacks(this)
                    //.addOnConnectionFailedListener(this)
                    .build();

            // Connect the client.
            if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }
    }


    private void drawMap(GoogleMap map) {
        this.map = map;
        if (map != null) {
            // Map is ready
            Toast.makeText(activity, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            //PlacesFragmentPermissionsDispatcher.getMyLocationWithCheck(this);
        } else {
            Toast.makeText(activity, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
//            if (errorDialog != null) {
//                // Create a new DialogFragment for the error dialog
//                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
//                errorFragment.setDialog(errorDialog);
//                errorFragment.show(sfm, "Location Updates");
//            }

            return false;
        }
    }
}

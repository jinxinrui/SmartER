package com.example.jxr.smarter;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// import libraries for map

import com.example.jxr.smarter.RestWS.RestClient;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;

// show absolute location on map
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by jxr on 27/4/18.
 */

public class MapFragment extends Fragment {
    View vMap;

    //private final LatLng SAN_FRAN = new LatLng(37.7749, -122.4194);

    private MapboxMap mMapboxMap;
    private MapView mMapView;

    private String userInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(getContext());

        vMap = inflater.inflate(R.layout.fragment_map, container, false);

        userInfo = getArguments().getString("userInfo");

        mMapView = (MapView) vMap.findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);

        new AsyncTask<String, Void, String[]>() {
            @Override
            protected String[] doInBackground(String... params) {
                String address = RestClient.getAddressSnippet(userInfo);
                address = RestClient.formatAddress(address); // format the address so it can be used by Google Geocoding
                String geoLocation = RestClient.getGeoLocation(address);
                String[] latAndLon = RestClient.getLatAndLon(geoLocation);
                String lat = latAndLon[0];
                String lon = latAndLon[1];
                return latAndLon; // result in onPostExecute is returned by this
            }
            @Override
            protected void onPostExecute(final String[] result) {
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        double lat = Double.parseDouble(result[0]);
                        double lon = Double.parseDouble(result[1]);
                        LatLng geo = new LatLng(lat, lon);
                        mMapboxMap = mapboxMap;
                        mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geo, 11));
                        addMarker(mMapboxMap, geo);
                    }
                });
            }
        }.execute(new String[] {null});



        return vMap;
    }

    @Override
    public void onResume()
    { super.onResume(); mMapView.onResume(); }
    @Override
    public void onPause()
    { super.onPause(); mMapView.onPause(); }

    @Override
    public void onDestroy()
    { super.onDestroy(); mMapView.onDestroy(); }
    @Override
    public void onSaveInstanceState(Bundle outState)
    { super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void addMarker(MapboxMap mapboxMap, LatLng geoLocation) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(geoLocation);
        markerOptions.title("Home");
        markerOptions.snippet("This is your house");
        mapboxMap.addMarker(markerOptions);
    }
}

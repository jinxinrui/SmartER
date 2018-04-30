package com.example.jxr.smarter;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

// import libraries for map

import com.example.jxr.smarter.RestWS.RestClient;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;

// show absolute location on map
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by jxr on 27/4/18.
 */

public class MapFragment extends Fragment {
    View vMap;

    //private final LatLng SAN_FRAN = new LatLng(37.7749, -122.4194);

    private MapboxMap mMapboxMap;
    private MapView mMapView;

    private String userInfo;
    private String resid;

    private Spinner mMapSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(getContext());
        vMap = inflater.inflate(R.layout.fragment_map, container, false);

        mMapSpinner = (Spinner) vMap.findViewById(R.id.mapSpinner);

        userInfo = getArguments().getString("userInfo");

        final String resident = RestClient.getResidentSnippet(userInfo);

        resid = RestClient.getResidSnippet(resident);


        mMapView = (MapView) vMap.findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);

        mMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String selection = mMapSpinner.getSelectedItem().toString();

                new AsyncTask<String, Void, String[]>() {
                    @Override
                    protected String[] doInBackground(String... params) {
                        String address = RestClient.getAddressSnippet(userInfo);
                        address = RestClient.formatAddress(address); // format the address so it can be used by Google Geocoding
                        String geoLocation = RestClient.getGeoLocation(address);
                        String[] latAndLon = RestClient.getLatAndLon(geoLocation);
                        String lat = latAndLon[0];
                        String lon = latAndLon[1];
                        String dailyUsage = RestClient.getUsageByOption(resid, "2018-03-08", "daily");
                        //String hourlyUsage = RestClient.getUsageByOption(resid,"2018-03-08", "hourly");
                        double randomUsage = Double.parseDouble(UsageSimulator.randomAir()) +
                                Double.parseDouble(UsageSimulator.randomFridge()) + Double.parseDouble(UsageSimulator.randomWash());
                        NumberFormat formatter = new DecimalFormat("#0.00");
                        String hourlyUsage = formatter.format(randomUsage);
                        String[] all = new String[]{lat, lon, dailyUsage, hourlyUsage};
                        return all; // result in onPostExecute is returned by this
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
                                mMapboxMap.clear();
                                if (selection.equals("Daily")){
                                    addMarker(mMapboxMap, geo, selection, result[2]);
                                } else {
                                    addMarker(mMapboxMap, geo, selection, result[3]);
                                }
                            }
                        });
                    }
                }.execute(new String[] {null});
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





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

    private void addMarker(MapboxMap mapboxMap, LatLng geoLocation, String title, String content) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(geoLocation);
        markerOptions.title(title);
        markerOptions.snippet(content);
        mapboxMap.addMarker(markerOptions);
    }
}

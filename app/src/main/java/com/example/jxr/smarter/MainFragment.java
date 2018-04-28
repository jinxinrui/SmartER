package com.example.jxr.smarter;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jxr.smarter.RestWS.RestClient;

/**
 * Created by jxr on 25/4/18.
 */

public class MainFragment extends Fragment {
    private View vMain;
    private TextView currentTempView;
    private TextView firstname;
    private String userInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);

        currentTempView = (TextView) vMain.findViewById(R.id.currentTempText);

        userInfo = getArguments().getString("userInfo");

        String helloName = RestClient.getFisrtnameSnippet(userInfo);

        firstname = (TextView) vMain.findViewById(R.id.helloNameText);
        firstname.setText(helloName);

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String address = RestClient.getAddressSnippet(userInfo);
                address = RestClient.formatAddress(address); // format the address so it can be used by Google Geocoding
                String geoLocation = RestClient.getGeoLocation(address);
                String[] latAndLon = RestClient.getLatAndLon(geoLocation);
                String lat = latAndLon[0];
                String lon = latAndLon[1];
                return RestClient.findTemp(lat,lon); // result in onPostExecute is returned by this
            }
            @Override
            protected void onPostExecute(String result) {
                String snippet = RestClient.getTempSnippet(result);
                currentTempView.setText(snippet);
            }
        }.execute(new String[] {null});

//        Factorial f = new Factorial();
//        f.execute(new String[] {null});
        return vMain;
    }

//    private class Factorial extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            return RestClient.findTemp(); // result in onPostExecute is returned by this
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            String snippet = RestClient.getSnippet(result);
//            currentTempView.setText(snippet);
//        }
//    }
}

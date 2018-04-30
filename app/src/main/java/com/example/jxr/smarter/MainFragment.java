package com.example.jxr.smarter;

import android.app.Fragment;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jxr.smarter.RestWS.RestClient;
import com.example.jxr.smarter.model.DBManager;
import com.example.jxr.smarter.model.ElectricityUsage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jxr on 25/4/18.
 */

public class MainFragment extends Fragment {
    private View vMain;
    private TextView currentTempView;
    private TextView firstname;
    private TextView usageCondition;
    private String userInfo;
    private DBManager dbManager;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);

        dbManager = new DBManager(getContext());

        currentTempView = (TextView) vMain.findViewById(R.id.currentTempText);

        userInfo = getArguments().getString("userInfo");

        String helloName = RestClient.getFisrtnameSnippet(userInfo);

        firstname = (TextView) vMain.findViewById(R.id.helloNameText);
        firstname.setText(helloName);

        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(currentTime);

        imageView = (ImageView) vMain.findViewById(R.id.alertImage);
        imageView.setImageResource(R.drawable.compass);
        String message = "Not in peak hour";
        double currentUsage = getCurrentUsage();
        if (time.compareTo("03:00:00") >= 0 && time.compareTo("22:00:00") <= 0) {
            if (currentUsage > 1.5) {
                message = "Good!";
                imageView.setImageResource(R.drawable.ic_menu_gallery);
            } else if (currentUsage <= 1.5 && currentUsage > 0){
                message = "Not Good!";
                imageView.setImageResource(R.drawable.ic_menu_send);
            }
        }
        usageCondition = (TextView) vMain.findViewById(R.id.messageTextView);
        usageCondition.setText(message);


        // display temperature
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


        return vMain;
    }

    public double getCurrentUsage() {
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double total = 0.00;
        ArrayList<ElectricityUsage> usageList =  dbManager.getUsageList();
        if (usageList.size() > 0) {
            ElectricityUsage usageEntry = usageList.get(usageList.size() - 1);
            String acUsage = usageEntry.getAcusage();
            String fridgeUsage = usageEntry.getFridgeusage();
            String washUsage = usageEntry.getWashusage();
            total = Double.parseDouble(acUsage) + Double.parseDouble(fridgeUsage) + Double.parseDouble(washUsage);
        }

        dbManager.close();
        return total;
    }

}

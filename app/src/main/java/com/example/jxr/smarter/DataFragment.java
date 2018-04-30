package com.example.jxr.smarter;

import android.app.Fragment;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jxr.smarter.RestWS.RestClient;
import com.example.jxr.smarter.model.DBManager;
import com.example.jxr.smarter.model.ElectricityUsage;
import com.example.jxr.smarter.model.Resident;

import java.util.ArrayList;

/**
 * Created by jxr on 29/4/18.
 */

public class DataFragment extends Fragment implements View.OnClickListener {
    private View vData;
    private DBManager dbManager;
    private TextView mDataTextView;
    private String userInfo;
    private ArrayList<ElectricityUsage> usageList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vData = inflater.inflate(R.layout.fragment_database, container, false);

        dbManager = new DBManager(getContext());

        Button mUploadButton = (Button) vData.findViewById(R.id.uploadButton);
        mUploadButton.setOnClickListener(this);

        Button mDropTableButton = (Button) vData.findViewById(R.id.dropTableButton);
        mDropTableButton.setOnClickListener(this);

        mDataTextView = (TextView) vData.findViewById(R.id.dataTextView);

        mDataTextView.setText(getUsageList());

        return vData;
    }

    public String getUsageList() {
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //ArrayList<ElectricityUsage> usageList =  dbManager.getUsageList();

        userInfo = getArguments().getString("userInfo");

        usageList = new ArrayList<>();
        Cursor cursor = dbManager.getAll();

        String residentString = RestClient.getResidentSnippet(userInfo);

        Resident currentResident = RestClient.convertResident(residentString);

        while (cursor.moveToNext()) {
            ElectricityUsage usageEntry = new ElectricityUsage(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    currentResident);
            usageList.add(usageEntry);
        }

        String data = "";

        for (int i = 0; i < usageList.size(); i++) {
            ElectricityUsage usageEntryString = usageList.get(i);
            data = data + usageEntryString.toString() + "\n";
        }

        dbManager.close();

        return data;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadButton:

                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        for (int i = 0; i < usageList.size(); i++) {
                            RestClient.createElecUsage(usageList.get(i));
                        }
                        return "";
                    }
                }.execute(new String[]{null});;


                break;

            case R.id.dropTableButton:
                try {
                    dbManager.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dbManager.removeAll();
                dbManager.close();
                break;

            default:
                break;
        }
    }


}

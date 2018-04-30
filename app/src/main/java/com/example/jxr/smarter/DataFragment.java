package com.example.jxr.smarter;

import android.app.Fragment;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jxr.smarter.model.DBManager;
import com.example.jxr.smarter.model.ElectricityUsage;

import java.util.ArrayList;

/**
 * Created by jxr on 29/4/18.
 */

public class DataFragment extends Fragment implements View.OnClickListener {
    private View vData;
    private DBManager dbManager;
    private TextView mDataTextView;

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

        ElectricityUsage usageEntry;

        ArrayList<ElectricityUsage> usageList =  dbManager.getUsageList();

        String data = "";

        for (int i = 0; i < usageList.size(); i++) {
            usageEntry = usageList.get(i);
            data = data + usageEntry.toString() + "\n";
        }

        dbManager.close();

        return data;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadButton:
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

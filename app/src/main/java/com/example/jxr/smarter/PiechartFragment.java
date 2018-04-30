package com.example.jxr.smarter;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * Created by jxr on 30/4/18.
 */

public class PiechartFragment extends Fragment {

    View vMain;

    private static  String TAG = "PiechartFragment";

    private float[] yData = {3.3f, 0.7f, 0.5f};

    private String[] xData = {"Air", "Washing", "Fridge"};

    private PieChart pieChart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vMain = inflater.inflate(R.layout.fragment_piechart, container,false);

        Log.d(TAG, "onCreate: starting to create chart");

        pieChart = (PieChart) vMain.findViewById(R.id.piechart);

        pieChart.setRotationEnabled(true);

        addDataSet();


        return vMain;
    }

    public void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++ ){
            yEntrys.add(new PieEntry(yData[i], i));
        }

        for (int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Usage");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.GRAY);
        colors.add(Color.RED);
        colors.add(Color.GREEN);



        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}

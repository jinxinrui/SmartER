package com.example.jxr.smarter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jxr on 28/4/18.
 */

public class LinechartFragment extends Fragment {

    View vReport;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vReport = inflater.inflate(R.layout.fragment_linechart, container,false);

        LineChart chart = (LineChart)vReport.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();

        //to display five values, and later formatter is used so years will not have decimal values
        float[] xAxis = {0f,1f,2f,3f,4f};
        float[] yAxis = {1, 0, 2, 3, 0};
        for (int i=0; i<xAxis.length; i++){
            entries.add(new Entry(xAxis[i], yAxis[i]));
        }

        //implementing IAxisValueFormatter interface to show year values not as float/decimal
        final String[] years = new String[] { "03-08", "03-09", "03-10", "03-11","03-12" };
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return years[(int)value];
            }
        };
        LineDataSet dataSet = new LineDataSet(entries, "Usage");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        XAxis xAxisFromChart = chart.getXAxis();
        xAxisFromChart.setDrawAxisLine(true);
        xAxisFromChart.setValueFormatter(formatter);
        // minimum axis-step (interval) is 1,if no, the same value will be displayed multiple times
        xAxisFromChart.setGranularity(1f);
        xAxisFromChart.setPosition(XAxis.XAxisPosition.BOTTOM);

        return vReport;
    }
}


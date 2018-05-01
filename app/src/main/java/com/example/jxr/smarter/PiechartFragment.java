package com.example.jxr.smarter;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import com.example.jxr.smarter.RestWS.RestClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jxr on 30/4/18.
 */

public class PiechartFragment extends Fragment implements View.OnClickListener {

    View vMain;

    private static  String TAG = "PiechartFragment";

    private EditText mDate;

    private String resid;

    private DatePickerDialog.OnDateSetListener datePicker;

    private Calendar myCalendar;

    private PieChart pieChart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vMain = inflater.inflate(R.layout.fragment_piechart, container,false);

        Log.d(TAG, "onCreate: starting to create chart");

        pieChart = (PieChart) vMain.findViewById(R.id.piechart);

        pieChart.setRotationEnabled(true);

        resid = getArguments().getString("resid");

        mDate = (EditText) vMain.findViewById(R.id.pieDateText);

        Button submitButton = (Button) vMain.findViewById(R.id.pieSubmitButton);

        submitButton.setOnClickListener(this);

        myCalendar = Calendar.getInstance();

        datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mDate.setOnClickListener(this);

        return vMain;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pieSubmitButton:
                final String currentDate = mDate.getText().toString();
                new AsyncTask<String, Void, float[]>() {
                    protected float[] doInBackground(String... params) {
                        String dailyUsage = RestClient.getDailyUsage(resid, currentDate);
                        String[] usageSnippet = RestClient.getDailyUsageSnippet(dailyUsage);
                        String air = usageSnippet[0];
                        String fridge = usageSnippet[1];
                        String wash = usageSnippet[2];
                        float airFloat = Float.parseFloat(air);
                        float fridgeFloat = Float.parseFloat(fridge);
                        float washFloat = Float.parseFloat(wash);
                        //addDataSet(airFloat, fridgeFloat, washFloat);
                        float [] usageArray = new float[]{airFloat, fridgeFloat, washFloat};
                        return usageArray;
                    }

                    protected void onPostExecute(float[] result) {
                        addDataSet(result[0], result[1], result[2]);
                    }
                }.execute(new String[] {null});
                break;

            case R.id.pieDateText:
                new DatePickerDialog(getContext(), datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }

    }

    public void addDataSet(float air, float fridge, float wash) {

        float totalP = air + wash + fridge;

        float airP = air/totalP *100;
        float washP = wash/totalP *100;
        float fridgeP = fridge/totalP *100;

        float[] yData = {airP, washP, fridgeP};

        String[] xData = {"Air", "Washing", "Fridge"};

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++ ){
            yEntrys.add(new PieEntry(yData[i], xData[i]));
        }

        for (int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Usage");
        //pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);


        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.GRAY);
        colors.add(Color.RED);
        colors.add(Color.GREEN);



        pieDataSet.setColors(colors);

        //add legend to chart
        //Legend legend = pieChart.getLegend();
        //legend.setForm(Legend.LegendForm.CIRCLE);


        //create pie data object

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh
    }

    public void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDate.setText(sdf.format(myCalendar.getTime()));
    }
}

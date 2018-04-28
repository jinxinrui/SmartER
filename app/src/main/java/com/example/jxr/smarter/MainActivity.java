package com.example.jxr.smarter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jxr.smarter.RestWS.RestClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //TextView currentTempView;
    private String userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        // userInfo -- json of user information
        userInfo = intent.getStringExtra("userInfo");

        MyThread mt = new MyThread();
        mt.start();


        // initialize EditText currentTemp
//        currentTempView = (TextView) findViewById(R.id.currentTempText);
//        Factorial f = new Factorial();
//        f.execute(new String[] {null});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("userInfo", userInfo);
        Fragment firstFragment = new MainFragment();
        firstFragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.content_frame, firstFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment nextFragment = null;
        Bundle bundle = new Bundle();

        switch (id) {
            case R.id.nav_home:

                bundle.putString("userInfo", userInfo);
                nextFragment = new MainFragment();
                nextFragment.setArguments(bundle);
                break;
            case R.id.nav_map_unit:

                bundle.putString("userInfo", userInfo);
                nextFragment = new MapFragment();
                nextFragment.setArguments(bundle);
                break;
            case R.id.nav_report_unit:
                nextFragment = new ReportFragment();
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        AsyncTask<Params, Progress, Result>
        Params -- the type of the parameters sent to the task(doInBackground()). It can be an array of objects
        Progress -- the type of the progress units published during the background computation(optional). Here I set it Void.
        Result -- the type of the result of the background computation(onPostExecute())
     */
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

    private class MyThread extends Thread {
        // random token decides if use airCon and washing machine or not
        private Random random = new Random();
        // washing machine token
        private Boolean washToken = true;
        // wash hour
        private int washHour = 0;
        // used for setting the Electricity usage ID
        private String usageId;
        // record number
        private int record;

        private int airCounter = 0;

        private int washCounter = 0;

        private int flag = 0;
        // the earliest time washing machine can work
        private String earlyStart = "06:00:00";
        // the latest time washing machine work
        private String lateStart = "19:00:00";

        private String earlyAir = "09:00:00";
        private String lateAir = "23:00:00";


        public MyThread() {

        }

        public void run() {
            String usageCount = RestClient.getUsageCount();
            usageId = String.valueOf(Integer.parseInt(usageCount) + 1);
            String address = RestClient.getAddressSnippet(userInfo);
            address = RestClient.formatAddress(address);
            String geoLocation = RestClient.getGeoLocation(address);
            String[] latAndLon = RestClient.getLatAndLon(geoLocation);
            String lat = latAndLon[0];
            String lon = latAndLon[1];
            while(!isInterrupted()){
                String acUsage = "0.00";
                String washUsage = "0.00";
                String fridgeUsage = "0.00";
                String tempRaw = RestClient.findTemp(lat ,lon);
                String temp = RestClient.getTempSnippet(tempRaw);
                double tempDouble = Double.parseDouble(temp);
                // Integer tempInt used for compare temperature
                int tempInt = (int) tempDouble;
                // String temp used for database and POST
                temp = String.valueOf(tempInt);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String dateString = sdf.format(date);

                if (washCounter == 0
                        && dateString.compareTo(earlyStart) == 1
                        && dateString.compareTo(lateStart) == -1
                        && washToken == true) {
                    if (random.nextBoolean() || washToken == true){
                        washUsage = UsageSimulator.randomWash();
                        washHour ++;
                        if (washHour > 3){
                            washCounter = 1;
                        }
                    }

                }else if(dateString.compareTo("21:00:00") == 1 && dateString.compareTo(earlyStart) == -1) {
                    washCounter = 0;
                }

                if (airCounter < 10 && tempInt >= 20
                        && dateString.compareTo(earlyAir) == 1
                        && dateString.compareTo(lateAir) == -1) {
                    if (random.nextBoolean()){
                        acUsage = UsageSimulator.randomAir();
                        airCounter ++;
                    }
                } else if (dateString.compareTo(lateAir) == 1 && dateString.compareTo(earlyAir) == -1) {
                    airCounter = 0;
                }



                fridgeUsage = UsageSimulator.randomFridge();
                if (record >= 24) {
                    record = 0;
                    // POST all records to back-end
                    // Drop Table
                }




                try {
                    Thread.sleep(1000*60*60);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dropTable() {

    }

}

package com.example.jxr.smarter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jxr.smarter.RestWS.RestClient;
import com.example.jxr.smarter.model.DBManager;
import com.example.jxr.smarter.model.ElectricityUsage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // userInfo is raw data get from http
    private String userInfo;

    ArrayList<ElectricityUsage> usageList = new ArrayList<>();

    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize database
        dbManager = new DBManager(this);


        Intent intent = getIntent();
        // userInfo -- json of user information
        userInfo = intent.getStringExtra("userInfo");


//        Button testButton = (Button) findViewById(R.id.thisButton);
//        testButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                TextView test = (TextView) findViewById(R.id.testTextView);
//                test.setText(readData());
//            }
//        });

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

        MyThread mt = new MyThread();
        mt.start();
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

            case R.id.nav_line_unit:
                nextFragment = new LinechartFragment();
                break;

            case R.id.nav_pie_unit:
                nextFragment = new PiechartFragment();
                break;

            case R.id.nav_database_unit:
                nextFragment = new DataFragment();
                break;



            default:
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





//private class extends new thread to record usage.
    private class MyThread extends Thread {
        // random token decides if use airCon and washing machine or not
        private Random random = new Random();
        // washing machine token
        private Boolean washToken = false;
        // wash hour
        private int washHour = 0;

        // record number
        private int records;

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
            // used for setting the Electricity usage ID
            String usageCount = RestClient.getUsageCount();
            int usageId = Integer.parseInt(usageCount) + 1;
            String usageIdString = String.valueOf(usageId);

            String resident = RestClient.getResidentSnippet(userInfo);

            String address = RestClient.getAddressSnippet(userInfo);
            address = RestClient.formatAddress(address);
            String geoLocation = RestClient.getGeoLocation(address);
            String[] latAndLon = RestClient.getLatAndLon(geoLocation);
            String lat = latAndLon[0];
            String lon = latAndLon[1];
            while(!isInterrupted()){
                // initialize usage
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
                String timeString = sdf.format(date);
                // record the hour
                String hour = getTime(timeString);

                // record the date
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String dayString = sdf1.format(date);

                if (washCounter == 0
                        && timeString.compareTo(earlyStart) > 0
                        && timeString.compareTo(lateStart) < 0
                        && washCounter < 3) {
                    if (washToken == true) {
                        washUsage = UsageSimulator.randomWash();
                        washCounter++;
                    } else if (random.nextBoolean()) {
                        washUsage = UsageSimulator.randomWash();
                        washCounter++;
                        washToken = true;
                    }
                } else if(timeString.compareTo("21:00:00") > 0 && timeString.compareTo(earlyStart) < 0) {
                    washCounter = 0;
                }

                if (airCounter < 10 && tempInt >= 20
                        && timeString.compareTo(earlyAir) > 0
                        && timeString.compareTo(lateAir) < 0) {
                    if (random.nextBoolean()){
                        acUsage = UsageSimulator.randomAir();
                        airCounter ++;
                    }
                } else if (timeString.compareTo(lateAir) > 0 && timeString.compareTo(earlyAir) < 0) {
                    airCounter = 0;
                }



                fridgeUsage = UsageSimulator.randomFridge();
                usageId = usageId + 1;

                // insert to database
                insertData(usageIdString, acUsage, fridgeUsage, washUsage, dayString, timeString, temp, resident);
                records = records + 1;

                if (records >= 24) {
                    records = 0;
                    // get all records in database
                    Cursor cursor = dbManager.getAll();
                    while(cursor.moveToNext()) {
                        ElectricityUsage usageEntry = new ElectricityUsage(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getString(7));
                        usageList.add(usageEntry);
                    }
                    cursor.close();

                    // POST all records to back-end
                    for (int i = 0; i < usageList.size(); i++) {
                        RestClient.createElecUsage(usageList.get(i));
                    }

                    // Drop Table
                    dbManager.removeAll();
                    // clear usageList
                    usageList.clear();
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

    public String getTime(String date) {
        String time = null;
        if (date.compareTo("00:00:00") >= 0 && date.compareTo("01:00:00") < 0) {
            time = "0";
        } else if (date.compareTo("01:00:00") >= 0 && date.compareTo("02:00:00") < 0) {
            time = "1";
        } else if (date.compareTo("02:00:00") >= 0 && date.compareTo("03:00:00") < 0) {
            time = "2";
        } else if (date.compareTo("03:00:00") >= 0 && date.compareTo("04:00:00") < 0) {
            time = "3";
        } else if (date.compareTo("04:00:00") >= 0 && date.compareTo("05:00:00") < 0) {
            time = "4";
        } else if (date.compareTo("05:00:00") >= 0 && date.compareTo("06:00:00") < 0) {
            time = "5";
        } else if (date.compareTo("06:00:00") >= 0 && date.compareTo("07:00:00") < 0) {
            time = "6";
        } else if (date.compareTo("07:00:00") >= 0 && date.compareTo("08:00:00") < 0) {
            time = "7";
        } else if (date.compareTo("08:00:00") >= 0 && date.compareTo("09:00:00") < 0) {
            time = "8";
        } else if (date.compareTo("09:00:00") >= 0 && date.compareTo("10:00:00") < 0) {
            time = "9";
        } else if (date.compareTo("10:00:00") >= 0 && date.compareTo("11:00:00") < 0) {
            time = "10";
        } else if (date.compareTo("11:00:00") >= 0 && date.compareTo("12:00:00") < 0) {
            time = "11";
        } else if (date.compareTo("12:00:00") >= 0 && date.compareTo("13:00:00") < 0) {
            time = "12";
        } else if (date.compareTo("13:00:00") >= 0 && date.compareTo("14:00:00") < 0) {
            time = "13";
        } else if (date.compareTo("14:00:00") >= 0 && date.compareTo("15:00:00") < 0) {
            time = "14";
        } else if (date.compareTo("15:00:00") >= 0 && date.compareTo("16:00:00") < 0) {
            time = "15";
        } else if (date.compareTo("16:00:00") >= 0 && date.compareTo("17:00:00") < 0) {
            time = "16";
        } else if (date.compareTo("17:00:00") >= 0 && date.compareTo("18:00:00") < 0) {
            time = "17";
        } else if (date.compareTo("18:00:00") >= 0 && date.compareTo("19:00:00") < 0) {
            time = "18";
        } else if (date.compareTo("19:00:00") >= 0 && date.compareTo("20:00:00") < 0) {
            time = "19";
        } else if (date.compareTo("20:00:00") >= 0 && date.compareTo("21:00:00") < 0) {
            time = "20";
        } else if (date.compareTo("21:00:00") >= 0 && date.compareTo("22:00:00") < 0) {
            time = "21";
        } else if (date.compareTo("22:00:00") >= 0 && date.compareTo("23:00:00") < 0) {
            time = "22";
        } else if (date.compareTo("23:00:00") >= 0 && date.compareTo("23:59:59") < 0) {
            time = "23";
        }
        return time;
    }

    // Database manipulation
    public void insertData(String usageid, String air, String fridge, String wash,
                           String day, String hour, String temp, String resid) {
        try {
            dbManager.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbManager.insertUsage(usageid, air, fridge, wash, day, hour, temp, resid);
        dbManager.close();
    }



//    public String readData() {
//        try {
//            dbManager.open();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        Cursor cursor = dbManager.getAll();
//        String s = "";
//        while(cursor.moveToNext()) {
//            s += cursor.getString(0) + cursor.getString(1) + cursor.getString(1);
//        }
//        dbManager.close();
//        return s;
//    }
}

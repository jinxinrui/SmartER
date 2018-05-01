package com.example.jxr.smarter.RestWS;

import android.util.Log;

import com.example.jxr.smarter.model.ElectricityUsage;
import com.example.jxr.smarter.model.Resident;
import com.example.jxr.smarter.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jxr on 24/4/18.
 */




public class RestClient {

    //private static final String USAGE_URI =

    // URI = "http://ip_address:11407/project_name/webresources"

//    private static final String USER_URI =
//            "http://118.139.60.181:11407/SmartER/webresources/entities.credential/findByCredential/";

    private static final String WEATHER_URI =
            "http://api.openweathermap.org/data/2.5/weather?"; //lat=-37.8770102&lon=145.0442693&appid=f93bd59bea3ab44fb8dba0d95596adfc

    private static final String GEO_URI =
            "https://maps.googleapis.com/maps/api/geocode/json?address=";

    private static final String BASE_URI =
            "http://192.168.1.101:11407/SmartER/webresources";

    private static final String KEY = "AIzaSyDTRZ-ftWCk71XZIN5xZ8-GNO3XT0HQ_a8";


    // GET temperature from internet
    public static String findTemp(String lat, String lon) {
        //final String methodPath = "/entities.electricityusage/";
        //initialize
        URL url = null;
        String appid = "appid=f93bd59bea3ab44fb8dba0d95596adfc";
        HttpURLConnection conn = null;
        String textResult = "";
        //making http request
        try {
            url = new URL(WEATHER_URI + "lat=" + lat + "&" + "lon=" + lon + "&" +appid);
            // open the connection
            conn = (HttpURLConnection) url.openConnection();
            // set the time out
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            // set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getTempSnippet(String result) {
        String snippet = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            String kelvin = jsonObject.getJSONObject("main").getString("temp");
            double celsius = Double.parseDouble(kelvin) - 273.15;
            snippet = Double.toString((int)celsius);
        } catch (Exception e) {
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }

    // GET user from RestWS
    public static String findUser(String username) {
        URL url = null;
        HttpURLConnection connection = null;
        String methodPath = "/entities.credential/findByCredential/";
        String textResult = "";
        try {
            url = new URL(BASE_URI + methodPath + username);

            connection = (HttpURLConnection) url.openConnection();
            // set the time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set the connection method to GET
            connection.setRequestMethod("GET");
            //add http headers to set your response type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(connection.getInputStream());
            //read the input stream and store it as string

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;
    }

    // get password snippet from the json object
    public static String getPassword(String result) {
        String snippet = null;
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            snippet = jsonObject.getString("passwordhash");
        } catch (Exception e) {
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }

    // GET the Resident count
    public static String getResidentCount() {
        URL url = null;
        String methodPath = "/entities.resident/count";
        HttpURLConnection connection = null;
        String textResult = "";
        try {
            url = new URL(BASE_URI + methodPath);

            connection = (HttpURLConnection) url.openConnection();
            // set the time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set the connection method to GET
            connection.setRequestMethod("GET");
            //add http headers to set your response type to json
            connection.setRequestProperty("Content-Type", "text/plain"); // type should conform to the type in back-end
            connection.setRequestProperty("Accept", "text/plain");
            //Read the response
            Scanner inStream = new Scanner(connection.getInputStream());
            //read the input stream and store it as string

//            while (inStream.hasNextLine()) {
//                textResult += inStream.nextLine();
//            }
            textResult = inStream.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;
    }

    // http POST for register
    public static void createResident(Resident resident) {
        // initialize
        URL url = null;
        HttpURLConnection connection = null;
        final String methodPath = "/entities.resident/";
        try {
            Gson gson = new Gson();
            String stringResidentJson = gson.toJson(resident);
            url = new URL(BASE_URI + methodPath);
            // open connection
            connection = (HttpURLConnection) url.openConnection();
            // set time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set connection method to POST
            connection.setRequestMethod("POST");
            // set the output to true
            connection.setDoOutput(true);
            // set length of the data you want to send
            connection.setFixedLengthStreamingMode(stringResidentJson.getBytes().length);
            // add HTTP headers
            connection.setRequestProperty("Content-Type", "application/json");

            // send the POST out
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(stringResidentJson);
            out.close();
            Log.i("error", new Integer(connection.getResponseCode()).toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    // Http POST for user
    public static void createUser(User user) {
        URL url = null;
        HttpURLConnection connection = null;
        final String methodPath = "/entities.credential/";
        try {
            Gson gson = new Gson();
            String stringUserJson = gson.toJson(user);
            url = new URL(BASE_URI + methodPath);
            // open connection
            connection = (HttpURLConnection) url.openConnection();
            // set time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set connection method to POST
            connection.setRequestMethod("POST");
            // set the output to true
            connection.setDoOutput(true);
            // set length of the data you want to send
            connection.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
            // add HTTP headers
            connection.setRequestProperty("Content-Type", "application/json");

            // send the POST out
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(stringUserJson);
            out.close();
            Log.i("error", new Integer(connection.getResponseCode()).toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public static String getUsageCount() {
        URL url = null;
        String methodPath = "/entities.electricityusage/count";
        HttpURLConnection connection = null;
        String textResult = "";
        try {
            url = new URL(BASE_URI + methodPath);

            connection = (HttpURLConnection) url.openConnection();
            // set the time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set the connection method to GET
            connection.setRequestMethod("GET");
            //add http headers to set your response type to json
            connection.setRequestProperty("Content-Type", "text/plain"); // type should conform to the type in back-end
            connection.setRequestProperty("Accept", "text/plain");
            //Read the response
            Scanner inStream = new Scanner(connection.getInputStream());
            //read the input stream and store it as string

//            while (inStream.hasNextLine()) {
//                textResult += inStream.nextLine();
//            }
            textResult = inStream.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;
    }

    // get address out of userInfo
    public static String getAddressSnippet(String userInfo) {
        String snippet = null;
        try {
            JSONArray jsonArray = new JSONArray(userInfo);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject resid = jsonObject.getJSONObject("resid");
            snippet = resid.getString("address");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snippet;
    }

    // format the address
    public static String formatAddress(String address) {
        // format e.g. "900+Dandenong+Road,+Caulfield+East, +VIC"
        address = address.replace(" ", "+");
        return address;
    }

    // raw data of geo location
    public static String getGeoLocation(String address) {
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try {
            url = new URL(GEO_URI + address + "&key=" + KEY);
            // open the connection
            conn = (HttpURLConnection) url.openConnection();
            // set the time out
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            // set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return textResult;
    }

    public static String[] getLatAndLon(String geoLocation) {
        String[] snippet = new String[2];
        try {
            JSONObject jsonObject = new JSONObject(geoLocation);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            JSONObject geometry = jsonObject1.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            String lat = location.getString("lat");
            String lng = location.getString("lng");
//            String lat = jsonObject.getJSONObject("results").getJSONObject("location").getString("lat");
//            String lng = jsonObject.getJSONObject("results").getJSONObject("location").getString("lng");
            snippet[0] = lat;
            snippet[1] = lng;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return snippet;
    }

    // get the first name of the user
    public static String getFisrtnameSnippet(String user) {
        String snippet = null;
        try {
            JSONArray jsonArray = new JSONArray(user);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject resid = jsonObject.getJSONObject("resid");
            snippet = resid.getString("firstname");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snippet;
    }


    // find usage by id, date and view options(daily/hourly)
    public static String getUsageByOption(String resid, String date, String option) {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String methodPath = "/entities.electricityusage/dailyUsage2/";
        try {
            url = new URL(BASE_URI + methodPath + resid + "/" + date + "/" + option);
            // open the connection
            connection = (HttpURLConnection) url.openConnection();
            // set the time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set the connection method to GET
            connection.setRequestMethod("GET");
            //add http headers to set your response type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(connection.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return textResult;
    }

    // get resident from user info
    public static String getResidentSnippet(String userInfo) {
        String snippet = null;
        try {
            JSONArray jsonArray = new JSONArray(userInfo);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String resident = jsonObject.getString("resid");
            snippet = resident;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snippet;
    }

    public static Resident convertResident(String jsonResident) {
        Resident newResident;
        String address = "";
        String dob = "";
        String email = "";
        String firstname = "";
        String mobile = "";
        String numofres = "";
        String postcode = "";
        String provider = "";
        String resid = "";
        String surname = "";
        try{
            JSONObject jsonObject = new JSONObject(jsonResident);
            address = jsonObject.getString("address");
            dob = jsonObject.getString("dob");
            email = jsonObject.getString("email");
            firstname = jsonObject.getString("firstname");
            mobile = jsonObject.getString("mobile");
            numofres = jsonObject.getString("numofres");
            postcode = jsonObject.getString("postcode");
            provider = jsonObject.getString("provider");
            resid = jsonObject.getString("resid");
            surname = jsonObject.getString("surname");

        } catch (Exception e) {
            e.printStackTrace();
        }
        newResident = new Resident(resid, firstname, surname, dob, address, postcode, email, mobile, numofres, provider);
        return newResident;
    }

    // get resident id snippet from resident
    public static String getResidSnippet(String resident) {
        String snippet = null;
        try {
            JSONObject jsonObject = new JSONObject(resident);
            snippet = jsonObject.getString("resid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snippet;
    }

    public static String getResidentByEmail(String email) {
        URL url = null;
        HttpURLConnection connection = null;
        String methodPath = "/entities.resident/findByEmail/";
        String textResult = "";
        try {
            url = new URL(BASE_URI + methodPath + email);

            connection = (HttpURLConnection) url.openConnection();
            // set the time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set the connection method to GET
            connection.setRequestMethod("GET");
            //add http headers to set your response type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(connection.getInputStream());
            //read the input stream and store it as string

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return textResult;
    }

    // Http POST for Electricity Usage
    public static void createElecUsage(ElectricityUsage electricityUsage) {
        URL url = null;
        HttpURLConnection connection = null;
        final String methodPath = "/entities.electricityusage/";
        try {
            Gson gson = new Gson();
            String stringUserJson = gson.toJson(electricityUsage);
            url = new URL(BASE_URI + methodPath);
            // open connection
            connection = (HttpURLConnection) url.openConnection();
            // set time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set connection method to POST
            connection.setRequestMethod("POST");
            // set the output to true
            connection.setDoOutput(true);
            // set length of the data you want to send
            connection.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
            // add HTTP headers
            connection.setRequestProperty("Content-Type", "application/json");

            // send the POST out
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(stringUserJson);
            out.close();
            Log.i("error", new Integer(connection.getResponseCode()).toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public static String getDailyUsage(String resid, String date){
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String methodPath = "/entities.electricityusage/dailyUsage1/";
        try {
            url = new URL(BASE_URI + methodPath + resid + "/" + date);
            // open the connection
            connection = (HttpURLConnection) url.openConnection();
            // set the time out
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            // set the connection method to GET
            connection.setRequestMethod("GET");
            //add http headers to set your response type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(connection.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return textResult;
    }

    public static String[] getDailyUsageSnippet(String dailyUsage) {
        String[] snippet = new String[] {"", "", ""};
        try {
            JSONArray jsonArray = new JSONArray(dailyUsage);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String air = jsonObject.getString("aircon");
            String fridge = jsonObject.getString("fridge");
            String wash = jsonObject.getString("washingmachine");
            snippet[0] = air;
            snippet[1] = fridge;
            snippet[2] = wash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snippet;
    }
}

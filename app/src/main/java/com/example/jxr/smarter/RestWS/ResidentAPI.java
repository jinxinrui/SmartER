//package com.example.jxr.smarter.RestWS;
//
//import android.util.Log;
//
//import com.example.jxr.smarter.model.Resident;
//import com.google.gson.Gson;
//
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//
///**
// * Created by jxr on 27/4/18.
// */
//
//public class ResidentAPI {
//
//    private static final String BASE_URI =
//            "http://118.139.60.181:11407/SmartER/webresources";
//
//    // GET the Resident count
//    public static String getResidentCount() {
//        URL url = null;
//        String methodPath = "/entities.resident/count";
//        HttpURLConnection connection = null;
//        String textResult = "";
//        try {
//            url = new URL(BASE_URI + methodPath);
//
//            connection = (HttpURLConnection) url.openConnection();
//            // set the time out
//            connection.setReadTimeout(10000);
//            connection.setConnectTimeout(15000);
//            // set the connection method to GET
//            connection.setRequestMethod("GET");
//            //add http headers to set your response type to json
//            connection.setRequestProperty("Content-Type", "text/plain"); // type should conform to the type in back-end
//            connection.setRequestProperty("Accept", "text/plain");
//            //Read the response
//            Scanner inStream = new Scanner(connection.getInputStream());
//            //read the input stream and store it as string
//
////            while (inStream.hasNextLine()) {
////                textResult += inStream.nextLine();
////            }
//            textResult = inStream.nextLine();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            connection.disconnect();
//        }
//        return textResult;
//    }
//
//    // http POST for register
//    public static void createResident(Resident resident) {
//        // initialize
//        URL url = null;
//        HttpURLConnection connection = null;
//        final String methodPath = "/entities.resident/";
//        try {
//            Gson gson = new Gson();
//            String stringResidentJson = gson.toJson(resident);
//            url = new URL(BASE_URI + methodPath);
//            // open connection
//            connection = (HttpURLConnection) url.openConnection();
//            // set time out
//            connection.setReadTimeout(10000);
//            connection.setConnectTimeout(15000);
//            // set connection method to POST
//            connection.setRequestMethod("POST");
//            // set the output to true
//            connection.setDoOutput(true);
//            // set length of the data you want to send
//            connection.setFixedLengthStreamingMode(stringResidentJson.getBytes().length);
//            // add HTTP headers
//            connection.setRequestProperty("Content-Type", "application/json");
//
//            // send the POST out
//            PrintWriter out = new PrintWriter(connection.getOutputStream());
//            out.print(stringResidentJson);
//            out.close();
//            Log.i("error", new Integer(connection.getResponseCode()).toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            connection.disconnect();
//        }
//    }
//}

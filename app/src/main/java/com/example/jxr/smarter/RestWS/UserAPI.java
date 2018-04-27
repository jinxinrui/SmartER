//package com.example.jxr.smarter.RestWS;
//
//import android.util.Log;
//
//import com.example.jxr.smarter.model.User;
//import com.google.gson.Gson;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
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
//public class UserAPI {
//
//    private static final String USER_URI =
//            "http://118.139.60.181:11407/SmartER/webresources/entities.credential/findByCredential/";
//
//    private static final String BASE_URI =
//            "http://118.139.60.181:11407/SmartER/webresources";
//
//
//    // GET user from RestWS
//    public static String findUser(String username) {
//        URL url = null;
//        HttpURLConnection connection = null;
//        String textResult = "";
//        try {
//            url = new URL(USER_URI + username);
//
//            connection = (HttpURLConnection) url.openConnection();
//            // set the time out
//            connection.setReadTimeout(10000);
//            connection.setConnectTimeout(15000);
//            // set the connection method to GET
//            connection.setRequestMethod("GET");
//            //add http headers to set your response type to json
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Accept", "application/json");
//            //Read the response
//            Scanner inStream = new Scanner(connection.getInputStream());
//            //read the input stream and store it as string
//
//            while (inStream.hasNextLine()) {
//                textResult += inStream.nextLine();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            connection.disconnect();
//        }
//        return textResult;
//    }
//
//    // Http POST for user
//    public static void createUser(User user) {
//        URL url = null;
//        HttpURLConnection connection = null;
//        final String methodPath = "/entities.credential/";
//        try {
//            Gson gson = new Gson();
//            String stringUserJson = gson.toJson(user);
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
//            connection.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
//            // add HTTP headers
//            connection.setRequestProperty("Content-Type", "application/json");
//
//            // send the POST out
//            PrintWriter out = new PrintWriter(connection.getOutputStream());
//            out.print(stringUserJson);
//            out.close();
//            Log.i("error", new Integer(connection.getResponseCode()).toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            connection.disconnect();
//        }
//    }
//
//    // get password snippet from the json object
//    public static String getPassword(String result) {
//        String snippet = null;
//        try {
//            JSONArray jsonArray = new JSONArray(result);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//            snippet = jsonObject.getString("passwordhash");
//        } catch (Exception e) {
//            e.printStackTrace();
//            snippet = "NO INFO FOUND";
//        }
//        return snippet;
//    }
//}

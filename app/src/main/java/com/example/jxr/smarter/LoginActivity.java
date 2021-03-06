package com.example.jxr.smarter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jxr.smarter.RestWS.RestClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLoginButton;

    private Button mRegisterButton;

    private EditText mUserEditText;

    private EditText mPasswordEditText;

    private String userInfo;

    // test api
    //private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mUserEditText = (EditText) findViewById(R.id.userEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);

        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(this);
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mRegisterButton.setOnClickListener(this);

        //test = (TextView) findViewById(R.id.testText);

    }

/*
        AsyncTask<Params, Progress, Result>
        Params -- the type of the parameters sent to the task(doInBackground()). It can be an array of objects
        Progress -- the type of the progress units published during the background computation(optional). Here I set it Void.
        Result -- the type of the result of the background computation(onPostExecute())
     */

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.loginButton:
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        return RestClient.findUser(params[0]); // result in onPostExecute is returned by this
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        String realPassword = RestClient.getPassword(result);
                        userInfo = result;

                        String inputPassword = StringHash.hashPassword(mPasswordEditText.getText().toString());

                        if (inputPassword.equals(realPassword)) {
                            goToMainPage(userInfo);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("Username or Password not correct!");
                            builder.setPositiveButton("OK", null);
                            builder.show();
                        }
                    }


                }.execute(new String[] {mUserEditText.getText().toString()});
                break;
            case R.id.registerButton:
                // need to get the number of residents in the back-end
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        return RestClient.getResidentCount(); // result in onPostExecute is returned by this
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        // pass resident count to register activity
                        goToRegisterPage(result);
                    }


                }.execute(new String[] {null});

                break;

            default:
                break;
        }
    }

    public void goToMainPage(String info) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userInfo", info);
        startActivity(intent);
    }

    public void goToRegisterPage(String info) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("Count", info);
        startActivity(intent);
    }

//    private class Factorial extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            String finalResult = RestClient.getPassword(result);
//            test.setText(finalResult);
//        }
//    }
}

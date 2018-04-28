package com.example.jxr.smarter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jxr.smarter.RestWS.RestClient;
import com.example.jxr.smarter.model.Resident;
import com.example.jxr.smarter.model.User;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mFirstname;
    private EditText mSurname;
    private EditText mDob;
    private EditText mAddress;
    private EditText mPostcode;
    private EditText mMobile;
    private EditText mEmail;
    private Spinner mNumofRes;
    private Spinner mProvider;
    private EditText mUsername;
    private EditText mPassword;

    private Integer resid;

    private Button mSubmitButton;

    private Calendar myCalendar;

    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // set the resid = count + 1, because no auto increment for generating resid in the backend
        Intent intent = getIntent();
        String countString = intent.getStringExtra("Count");

        int count = Integer.parseInt(countString) + 1;

        resid = count;

        mFirstname = (EditText) findViewById(R.id.firstname);
        mSurname = (EditText) findViewById(R.id.surname);
        mDob = (EditText) findViewById(R.id.birthday);
        mAddress = (EditText) findViewById(R.id.address);
        mPostcode = (EditText) findViewById(R.id.postcode);
        mMobile = (EditText) findViewById(R.id.mobile);
        mEmail = (EditText) findViewById(R.id.email);
        mNumofRes = (Spinner) findViewById(R.id.numofRes);
        mProvider = (Spinner) findViewById(R.id.provider);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);

        mSubmitButton = (Button) findViewById(R.id.submitButton);

        mSubmitButton.setOnClickListener(this);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

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

        mDob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                String fName = mFirstname.getText().toString();
                String sName = mSurname.getText().toString();
                String dob = mDob.getText().toString();
                String address = mAddress.getText().toString();
                String postcode = mPostcode.getText().toString();
                String mobile = mMobile.getText().toString();
                final String email = mEmail.getText().toString();
                String numofRes = mNumofRes.getSelectedItem().toString();
                String provider = mProvider.getSelectedItem().toString();
                final String username = mUsername.getText().toString();
                final String password = mPassword.getText().toString();

                if (empty(fName, sName, dob, address, postcode, email, mobile, numofRes, provider)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Alert");
                    builder.setMessage("Not all fields are filled!");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                } else {
                    final Resident resident = new Resident(resid, fName, sName, dob, address,
                            postcode, email, mobile, numofRes, provider);
                    // POST user to the back-end
                    new AsyncTask<String, Void, String>() {
                        @Override
                        // cannot manipulate UI elements inside doInBackground
                        protected String doInBackground(String... params) {
                            String message = "";
                            String userDb = RestClient.findUser(username);
                            String emailDb = RestClient.getResidentByEmail(email);
                            // check if username or email exist
                            if (!userDb.isEmpty() && !userDb.equals("[]")) {
                                message = "Username";
                            } else if (!emailDb.isEmpty() && !emailDb.equals("[]")) {
                                message = "Email";
                            } else {
                                RestClient.createResident(resident);

                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date date = new Date();
                                String currentDate = dateFormat.format(date);

                                User user = new User(username, password, currentDate, resident);
                                RestClient.createUser(user);
                            }

                            return message;
                        }
                        @Override
                        protected void onPostExecute(String result) {
                            if(!result.isEmpty()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("Alert");
                                builder.setMessage(result + " already exists!");
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            } else {
                                goToLogin();
                            }
                        }
                    }.execute(new String[]{null});
                }
                break;

            default:
                break;
        }
    }

    public void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDob.setText(sdf.format(myCalendar.getTime()));
    }

    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public boolean empty(String fName, String sName, String dob, String address,
                         String postcode, String email, String mobile, String numofRes, String provider) {
        boolean notFilled = false;
        if (fName.trim().isEmpty() || sName.trim().isEmpty() || dob.trim().isEmpty() || address.trim().isEmpty() ||
                postcode.trim().isEmpty() || email.trim().isEmpty() || mobile.trim().isEmpty()) {
            notFilled = true;
        }
        return notFilled;
    }
}

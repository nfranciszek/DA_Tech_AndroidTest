package com.datechnologies.androidtest.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.transform.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * A screen that displays a login prompt, allowing the user to login to the D & A Technologies Web Server.
 *
 */
public class LoginActivity extends AppCompatActivity {


    private final String LOGIN_URL = "http://dev.rapptrlabs.com/Tests/scripts/login.php";

    private String EMAIL_KEY;
    private String PASSWORD_KEY;
    private String EMPTY_STRING = "";
    private String email_stored = "info@rapptrlabs.com";
    private String password_stored = "Test123";
    private String CODE_RL;
    private String MESSAGE_RL;


    long startTime;
    long stopTime;
    long endTime = (stopTime - startTime);



    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context)
    {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================


    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginButton;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // TODO: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.
        // TODO: Add a ripple effect when the buttons are clicked
        // TODO: Save screen state on screen rotation, inputted username and password should not disappear on screen rotation

        // TODO: Send 'email' and 'password' to http://dev.rapptrlabs.com/Tests/scripts/login.php
        // TODO: as FormUrlEncoded parameters.

        // TODO: When you receive a response from the login endpoint, display an AlertDialog.
        // TODO: The AlertDialog should display the 'code' and 'message' that was returned by the endpoint.

        // TODO: The AlertDialog should also display how long the API call took in milliseconds.
        // TODO: When a login is successful, tapping 'OK' on the AlertDialog should bring us back to the MainActivity

        // TODO: The only valid login credentials are:
        // TODO: email: info@rapptrlabs.com
        // TODO: password: Test123
        // TODO: so please use those to test the login.





        mLoginEmailField = findViewById(R.id.EmailEditText);
        mLoginPasswordField = findViewById(R.id.PasswordEditText);
        mLoginButton = findViewById(R.id.LoginButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EMAIL_KEY = mLoginEmailField.getText().toString();
                PASSWORD_KEY = mLoginPasswordField.getText().toString();

                if (validateInputs()) {

                    startTimer();
                    login();
                }

            }
        });

    }


    private boolean validateInputs() {
        if(EMAIL_KEY.equals(EMPTY_STRING)){
            mLoginEmailField.setError("Email field cannot be empty");
            mLoginEmailField.requestFocus();
            return false;
        }
        if(PASSWORD_KEY.equals(EMPTY_STRING)){
            mLoginPasswordField.setError("Password field cannot be empty");
            mLoginPasswordField.requestFocus();
            return false;
        } else if (!EMAIL_KEY.contentEquals(email_stored)){
            mLoginEmailField.setError("Email not registered");
            mLoginEmailField.requestFocus();
            return false;


        } else if (!PASSWORD_KEY.contentEquals(password_stored)) {
            mLoginPasswordField.setError("Incorrect password");
            mLoginPasswordField.requestFocus();
            return false;

        }
        return true;
    }


    public class LoginRequest extends StringRequest {


        private Map<String, String> parameters;
        LoginRequest(String username, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(Method.POST, LOGIN_URL, listener, errorListener);
            parameters = new HashMap<>();
            parameters.put("email", username);
            parameters.put("password", password);
        }
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return parameters;
        }
    }


    private void login() {


        LoginRequest loginRequest = new LoginRequest(EMAIL_KEY, PASSWORD_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                        try {


                            JSONObject jsonObject = new JSONObject(response);

                           if (jsonObject.getString("code") != EMPTY_STRING) {


                               CODE_RL = jsonObject.getString("code");
                               MESSAGE_RL = jsonObject.getString("message");

                               stopTimer();
                               showResultDialog();


                           }  else {

                               showMessage("Bad login not successful :(");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginRequest);



    }



    private void showResultDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder
                .setTitle("Code: " + CODE_RL);
                builder.setMessage("Message: " + MESSAGE_RL + "  " + "API Duration: " + endTime)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        onBackPressed();

                    }
                });

        builder.create();
        builder.show();
    }

    private void startTimer() {

        startTime = System.currentTimeMillis();

    }

    private void stopTimer() {

        stopTime = System.currentTimeMillis();

        endTime = (stopTime - startTime);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }



}

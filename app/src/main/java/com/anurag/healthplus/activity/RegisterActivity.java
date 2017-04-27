/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.anurag.healthplus.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import info.androidhive.loginandregistration.R;
//import info.androidhive.loginandregistration.app.AppConfig;
//import info.androidhive.loginandregistration.app.AppController;
//import info.androidhive.loginandregistration.helper.SQLiteHandler;
//import info.androidhive.loginandregistration.helper.SessionManager;

import com.anurag.healthplus.R;
import com.anurag.healthplus.app.AppController;
import com.anurag.healthplus.app.AppConfig;

public class RegisterActivity extends Activity {
    private static final String TAG = "debug";
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFirstName,inputLastName,inputAddress;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
//    private SessionManager session;
//    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFirstName = (EditText) findViewById(R.id.name);
        inputLastName = (EditText) findViewById(R.id.name1);
        inputEmail = (EditText) findViewById(R.id.email);
        inputAddress = (EditText) findViewById(R.id.address);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

//        // Session manager
//        session = new SessionManager(getApplicationContext());
//
//        // SQLite database handler
//        db = new SQLiteHandler(getApplicationContext());
//
//        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(RegisterActivity.this,
//                    MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String fname = inputFirstName.getText().toString().trim();
                String lname = inputLastName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String address = inputAddress.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!fname.isEmpty() && !lname.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(fname, lname, email, address, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name,final String lname, final String email, final String address, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {//request to call server with register query

            @Override
            public void onResponse(String response) {//this is triggered when server replies
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                // Launch login activity
                if(response.equals("1")){//successfully registered.. redirect to login page
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                if (response.equals("2")) {
                        Toast.makeText(RegisterActivity.this, "Please input correct name.. try again...", Toast.LENGTH_LONG).show();
                    }
                if(response.equals("3")) {
                    Toast.makeText(RegisterActivity.this, "Some error occured while registering.. try again...", Toast.LENGTH_LONG).show();
                }
                if(response.equals("0")) {
                            Toast.makeText(RegisterActivity.this, "Some error occured while registering.. try again...", Toast.LENGTH_LONG).show();
                   }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", name);
                params.put("lname" , lname);
                params.put("email", email);
                params.put("address",address);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

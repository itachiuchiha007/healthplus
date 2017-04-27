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

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;

    String urlTologin = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/login.php";
    String patient_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
//        db = new SQLiteHandler(getApplicationContext());
//
//        // Session manager
//        session = new SessionManager(getApplicationContext());

//        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }



        });

        // Link to Register Screen111111111111111
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("anu", "Login Response: " + response);
                Toast.makeText(LoginActivity.this, "response:" + response, Toast.LENGTH_LONG).show();
                hideDialog();

                // Launch main activity
                if(response.equals("0")) {
                    Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_LONG).show();

                }
                else{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Log.d(TAG,"value of response : " + response);
                    intent.putExtra("patient_id",response);
                    startActivity(intent);
                    finish();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
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

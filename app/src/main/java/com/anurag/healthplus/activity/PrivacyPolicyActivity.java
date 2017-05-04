package com.anurag.healthplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.anurag.healthplus.R;
import com.anurag.healthplus.app.AppConfig;
import com.anurag.healthplus.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrivacyPolicyActivity extends AppCompatActivity {

    EditText editText1,editText2,editText3,editText4;
    Button button;
    RequestQueue requestQueue;
    String url = "http://athena.nitc.ac.in/balmukund_b130168cs/healthplus/change.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText1 = (EditText) findViewById(R.id.email1);
        editText2 = (EditText) findViewById(R.id.patientId12);
        editText3 = (EditText) findViewById(R.id.newpassword);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String email = editText1.getText().toString().trim();
               String newpassword = editText3.getText().toString().trim();

                String id = editText2.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !newpassword.isEmpty()&& !id.isEmpty()) {
                    // login user
                    checkLogin(email, newpassword,id);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

    private void checkLogin(final String email, final String newpassword,final String id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",email);
            jsonObject.put("id",id);
            jsonObject.put("newpassword",newpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

        Intent i = new Intent(PrivacyPolicyActivity.this,
                LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

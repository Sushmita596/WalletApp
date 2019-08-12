package com.example.sushmitakumari.loginregisteractivity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

//import static android.content.ContentValues.TAG;

/**
 * Created by sushmita.kumari on 16-01-2018.
 */

public class GetBalance extends AppCompatActivity{


    Float balance;
    private static final String URL_FOR_GETBALANCE = "http://172.20.10.13:8000/getBalance";//"http://192.168.56.101:8000/login";

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Float getBalance() {
        return balance;
    }

    public void httpGetBalance(String phoneNumber, String name, String email, Float amount, String bankTransId) {
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_GETBALANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("GetBalance Class", "Get balance response: " + response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    balance = Float.valueOf(jsonObject.getString("Amount"));
                    Log.d("GetBalance Class", "balance  = " + balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GetBalance Class", "Get Balance Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


        );
    }


}

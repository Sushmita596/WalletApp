package com.example.sushmitakumari.loginregisteractivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class AddBalanceActivity extends AppCompatActivity {

    private static final String TAG = "AddBalance";
    private static final String URL_FOR_ADDBALANCE = "http://172.27.193.116:8000/addMoney";
    ProgressDialog progressDialog;
    private EditText mAmount, loginInputPassword;
    private Button submit;
    private String number = null;
    private String name = null;
    //private Button btnLinkSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        mAmount = (EditText) findViewById(R.id.enterAmount);
        submit = (Button) findViewById(R.id.submit_Button);
        Bundle bundle = getIntent().getExtras();
        number = bundle.getString("phonenumber");
        name = bundle.getString("username");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBalance(Float.parseFloat(mAmount.getText().toString()), number, name);
            }
        });
    }

    private void addBalance(final float amount, final String phoneNumber, final String userName){
        String cancel_req_tag = "addbalance";
        progressDialog.setMessage("Adding Balance in Wallet...");
        //showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_ADDBALANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Add Balance Response: " + response.toString());
                try {
                    //JSONObject jObj = new JSONObject(response);
                    //if (Objects.equals(response, "Money Added Successfully")) {

                    Intent intent = new Intent(AddBalanceActivity.this, UserActivity.class);
                    intent.putExtra("username", name);
                    intent.putExtra("phonenumber", number);
                    startActivity(intent);
                    finish();
                    //} else {
                        //Log.d("Add Balance Activity", "failed to add money");
                    //}

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Add Balance Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override

            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("phoneNumber", phoneNumber);
                    jsonParam.put("addamount", amount);
                    //jsonParam.put("longitude", 0D);
                    Log.d("AddBalance JSON", jsonParam.toString());
                    return jsonParam.toString().getBytes("UTF-8");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.i("JSON", jsonParam.toString());

                return null;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }
}

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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "http://172.27.193.116:8000/login";//"http://192.168.56.101:8000/login";
    ProgressDialog progressDialog;
    private EditText loginInputPhone, loginInputPassword;
    private Button btnlogin;
    private Button btnLinkSignup;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInputPhone = (EditText) findViewById(R.id.input_phone_number);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        btnLinkSignup = (Button) findViewById(R.id.btn_link_signup);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.signup_input_password, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.passworderror);
        awesomeValidation.addValidation(this, R.id.phone_number, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    loginUser(loginInputPhone.getText().toString(),
                            loginInputPassword.getText().toString());
                }
            }
        });

        btnLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }
        });
    }

    private void loginUser( final String phone, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                showDialog();
                try {
                    JSONObject jObj = new JSONObject(response);

                        //String user = jObj.getJSONObject("user").getString("name");
                    String name = jObj.getString("name");
                    String phoneNumber = jObj.getString("phonenumber");

                    User details = new User();
                    details.setName(name);
                    details.setPhonenumber(phoneNumber);

                    Log.d(TAG, "name = "+details.getName()+" number = "+details.getPhonenumber());

                        // Launch User activity
                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                        intent.putExtra("username", name);
                        intent.putExtra("phonenumber", phoneNumber);
                        startActivity(intent);
                        finish();

                } catch (JSONException e) {
                    e.printStackTrace();
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
            /*protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Username", phone);
                params.put("Password", password);
                return params;
            }*/
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("username", phone);
                    jsonParam.put("password", password);
                    //jsonParam.put("longitude", 0D);
                    Log.d("JSON", jsonParam.toString());
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

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}

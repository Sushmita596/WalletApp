package com.example.sushmitakumari.loginregisteractivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    //private static final String URL_FOR_REGISTRATION = "https://api.androidhive.info/volley/person_array.json";
    private static final String URL_FOR_REGISTRATION = "http://172.27.193.21:8000/newuser"; //"http://192.168.56.101:8000/newuser";
    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword, signupConfirmPassword, signupInputPhone;
    private Button btnSignUp;
    private Button btnLinkLogin;
    // private RadioGroup genderRadioGroup;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        VolleyLog.DEBUG = true;


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        //signupInputAge = (EditText) findViewById(R.id.signup_input_age);
        signupInputPhone = (EditText) findViewById(R.id.phone_number);
        signupConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        //btnLinkLogin = (Button) findViewById(R.id.btn_link_login);

        addValidationToViews();

        // genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

    }

    private void addValidationToViews() {

        awesomeValidation.addValidation(this, R.id.signup_input_name, RegexTemplate.NOT_EMPTY,  R.string.nameerror);

        awesomeValidation.addValidation(this, R.id.signup_input_email, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        String regexPassword = ".{6,}";
        awesomeValidation.addValidation(this, R.id.signup_input_password, regexPassword, R.string.passworderror);
        awesomeValidation.addValidation(this, R.id.input_confirm_password, R.id.signup_input_password, R.string.confirmpaswrd);
        awesomeValidation.addValidation(this, R.id.phone_number, "^[+]?[0-9]{10,13}$", R.string.mobileerror);
    }


    private void submitForm() {

        if (awesomeValidation.validate()) {

            registerUser(signupInputName.getText().toString(),
                    signupInputEmail.getText().toString(),
                    signupInputPassword.getText().toString(),
                    signupInputPhone.getText().toString());
            //signupConfirmPassword.getText().toString());
        }
    }

    private void registerUser(final String name,  final String email, final String password, final String phoneNumber) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();
        Log.d(TAG,"Details sending : " +name  +phoneNumber  +email  +password);
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                    try {
                       // JSONObject jObj = new JSONObject(response);
                        //boolean error = jObj.getBoolean("error");

                           // String user = jObj.getJSONObject("users").getString("name");
                         /*User details = new User();
                         details.setName(name);
                         details.setEmail(email);
                         details.setPhonenumber(phoneNumber);*/
                            Toast.makeText(getApplicationContext(), "Hi , You are successfully Added!", Toast.LENGTH_SHORT).show();

                            // Launch login activity
                            Intent intent = new Intent(RegisterActivity.this, OTPScreen.class);
                            startActivity(intent);
                            finish();

                    } catch (Exception e) {
                        e.printStackTrace();
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
            /*@Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("emailid", email);
                params.put("password", password);
                params.put("phonenumber", phoneNumber);
                //params.put("confirmPassword", confirmPassword);
                Log.d(TAG,"params:" +params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }*/
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("name", name);

                    jsonParam.put("emailid",email);
                    jsonParam.put("phonenumber", phoneNumber);
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
        Log.d(TAG,"AppSingleton getinstance calling\n");
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
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

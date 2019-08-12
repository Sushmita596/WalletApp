package com.example.sushmitakumari.loginregisteractivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class OTPScreen extends AppCompatActivity implements View.OnClickListener{

    EditText enter_otp;
    Button resend, verify;
    ImageView back_button;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);

        enter_otp = (EditText) findViewById(R.id.enterOtp);
        resend = (Button) findViewById(R.id.resendOtp);
        verify = (Button) findViewById(R.id.verify_button);
        back_button = (ImageView) findViewById(R.id.imgBackArrow);
        back_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imgBackArrow:
                super.onBackPressed();
                finish();
                break;

            case R.id.resendOtp:
                //call backend otp generation api
                break;

            case R.id.verify_button:
                Intent intent = new Intent(OTPScreen.this, LoginActivity.class);
                startActivity(intent);
                break;

        }

    }

}

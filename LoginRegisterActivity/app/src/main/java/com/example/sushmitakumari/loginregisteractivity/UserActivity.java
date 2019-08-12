package com.example.sushmitakumari.loginregisteractivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Created by sushmita.kumari on 21-12-2017.
 */

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "UserActivity";
    private static final String URL_FOR_GETBALANCE = "http://172.27.193.116:8000/getBalance";

    private TextView greetingTextView, accountBalanceInfo;
    private Button btnLogOut;
    public ImageView addBalance, moneyTransfer, recentTransaction;
    private Float balance;
    String number = null;
    String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bundle bundle = getIntent().getExtras();
        //User userDetails = new User();
        name = bundle.getString("username");
        number = bundle.getString("phonenumber");

        httpGetBalance(number, null, null, (float) 0.00, null);

        greetingTextView = (TextView) findViewById(R.id.greeting_text_view);
        accountBalanceInfo = (TextView) findViewById(R.id.getbalance);
        btnLogOut = (Button) findViewById(R.id.logout_button);

        addBalance = (ImageView) findViewById(R.id.imgAddMoney);
        addBalance.setOnClickListener(this);
        moneyTransfer = (ImageView) findViewById(R.id.imgMoneyTransfer);
        moneyTransfer.setOnClickListener(this);
        recentTransaction = (ImageView) findViewById(R.id.imgRecentTransaction);
        recentTransaction.setOnClickListener(this);

        greetingTextView.setText(name + "\n" + number);
        Log.d("UserActivity", "name = "+name+" phone number = "+number);

        //String myBalance=Float.toString(balance);
       // Log.d("GetBalance", "balance before setText = "+balance);


        //accountBalanceInfo.setText(Float.floatToIntBits(balance));
        Log.d("UserActivity", "accountBalanceInfo = "+String .valueOf(balance));

        // Progress dialog
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgAddMoney:
                Toast.makeText(getApplicationContext(), "Add Balance clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserActivity.this, AddBalanceActivity.class);
                intent.putExtra("phonenumber", number);
                intent.putExtra("username", name);
                startActivity(intent);
                finish();
                break;

            case R.id.imgMoneyTransfer:
                Toast.makeText(getApplicationContext(), "Transfer Money is clicked", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(UserActivity.this, TransferBalanceActivity.class);
                intent1.putExtra("phonenumber", number);
                intent1.putExtra("username", name);
                intent1.putExtra("payerbalance", balance);
                startActivity(intent1);
                finish();
                break;

            case R.id.imgRecentTransaction:
                Toast.makeText(getApplicationContext(), "Recent transaction is clicked", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(UserActivity.this, TransactionHistoryActivity.class);
                intent2.putExtra("phonenumber", number);
                intent2.putExtra("username", name);
                intent2.putExtra("payerbalance", balance);
                startActivity(intent2);
                break;
        }
    }


    public void httpGetBalance(final String phoneNumber, String name, String email, final Float amount, String bankTransId) {
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_GETBALANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Balance Response: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);

                    balance = BigDecimal.valueOf(jObj.getDouble("addamount")).floatValue();
                    //balance = Float.valueOf(jObj.getString("Amount"));
                    Log.d("GET BALANCE CLASS", "balance = "+balance);
                    accountBalanceInfo.setText(""+balance);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Balance Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {
            @Override

            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("phoneNumber", phoneNumber);
                    jsonParam.put("addamount", amount);
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
        String cancel_req_tag = "getBalance";
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

}

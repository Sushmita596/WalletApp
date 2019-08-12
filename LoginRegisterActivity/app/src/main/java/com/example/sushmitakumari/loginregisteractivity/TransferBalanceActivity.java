package com.example.sushmitakumari.loginregisteractivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.view.MenuItem.OnMenuItemClickListener;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public class TransferBalanceActivity extends AppCompatActivity implements View.OnClickListener{

    Button transferBalance;
    EditText phoneNumber, transferAmount;
    String number = null;
    String name = null;
    private Float payerBalance;
    private static final String URL_FOR_TRANSFERBALANCE = "http://172.27.193.116:8000/transferfunds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_balance);
        transferBalance = (Button) findViewById(R.id.transfer_button);
        transferBalance.setOnClickListener(this);
        phoneNumber = (EditText) findViewById(R.id.enter_phone_number);
        transferAmount = (EditText) findViewById(R.id.amount_to_transfer);

        Bundle bundle = getIntent().getExtras();
        number = bundle.getString("phonenumber");
        name = bundle.getString("username");
        payerBalance = bundle.getFloat("payerbalance");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.transfer_button){
            //TODO: Goto ccAvenue page
            //MenuItem item = null;
            PopupMenu popup = new PopupMenu(TransferBalanceActivity.this, transferBalance);
            //Inflating the Popup using xml file
            //Log.d("TransferBalanceActivity", "getMenu = ");
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    //Toast.makeText(TransferBalanceActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                    switch (item.getItemId()){
                        case R.id.instant:
                            //TODO: Call trnasfer method
                            httpTrnasferBalanceForInstant(number, payerBalance, null, null,
                                    phoneNumber.getText().toString(), (float) 0.00, null, null,
                                    Float.parseFloat(transferAmount.getText().toString()), 0);
                            break;
                        case R.id.holdey:
                            //TODO: Call transfer method
                            httpTrnasferBalanceForInstant(number, payerBalance, null, null,
                                    phoneNumber.getText().toString(), (float) 0.00, null, null,
                                    Float.parseFloat(transferAmount.getText().toString()), 1);
                    }
                    return true;
                }
            });

            popup.show();//showing popup menu
        }
    }

    public void httpTrnasferBalanceForInstant(final String payerNumber, final Float payerBalance, String payerName, String payerBanckTransactionId,
                                              final String payeeNumber, final Float payeeBalance, String payeeName, String payeeBankTransactionId,
                                              final float mtransferAmount, final int holdiey) {

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_TRANSFERBALANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TransferBalanceActivity", "Balance Response: " + response);
                try {
                    Intent intent = new Intent(TransferBalanceActivity.this, UserActivity.class);
                    intent.putExtra("username", name);
                    intent.putExtra("phonenumber", number);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TransferBalanceActivity", "Balance Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {
            @Override

            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParam = new JSONObject();
                try {
                    JSONObject payer = new JSONObject();
                    payer.put("phoneNumber", payerNumber);
                    payer.put("addamount", payerBalance);
                    jsonParam.put("payer", payer);

                    JSONObject payee = new JSONObject();
                    payee.put("phoneNumber", payeeNumber);

                   // payee.put("addamount", amount);
                    jsonParam.put("payee", payee);

                    jsonParam.put("amount", mtransferAmount);
                    jsonParam.put("hold", holdiey);
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

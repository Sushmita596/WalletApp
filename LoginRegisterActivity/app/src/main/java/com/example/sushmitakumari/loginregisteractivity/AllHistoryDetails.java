package com.example.sushmitakumari.loginregisteractivity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllHistoryDetails extends ListActivity {

    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> historyList;
    JSONArray data = null;

    String number = null;
    String name = null;
    Float amount = (float)0.0;

    static InputStream is = null;
    static String json = "";
    JSONParser parser = new JSONParser();
    JSONArray reponses = null;

    private static final String TAG_MESSAGES = "response";
    private static final String TAG_ID = "transactid";
    private static final String TAG_FROM = "String";
    private static final String TAG_EMAIL = "date";
    private static final String TAG_SUBJECT = "Valid";

    private static final String URL_TRANSACTION_HISTORY = "http://172.27.193.116:8000/transactionhistory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_history_details);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("username");
        number = bundle.getString("phonenumber");
        amount = bundle.getFloat("payerbalance");
        allHistoryDetails(number, 0, null);
    }

    private void allHistoryDetails(final String phoneNumber, final int index, final String transactionId) {
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_TRANSACTION_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AllHistoryDetails", "AllHistory Response: " + response.toString());
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));

                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        is.close();
                        json = sb.toString();
                    } catch (Exception e) {
                        Log.e("Buffer Error", "Error converting result " + e.toString());
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        reponses = jsonObject.getJSONArray(TAG_MESSAGES);

                        // looping through All messages
                        for (int i = 0; i < reponses.length(); i++) {
                            JSONObject c = reponses.getJSONObject(i);

                            // Storing each json item in variable
                            String id = c.getString(TAG_ID);
                            String from = c.getString(TAG_FROM);
                            String subject = c.getString(TAG_SUBJECT);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_ID, id);
                            map.put(TAG_FROM, from);
                            map.put(TAG_SUBJECT, subject);

                            // adding HashList to ArrayList
                            historyList.add(map);

                            ListAdapter adapter = new SimpleAdapter(
                                    AllHistoryDetails.this, historyList,
                                    R.layout.all_history_list_item, new String[] { TAG_FROM, TAG_SUBJECT , TAG_EMAIL},
                                    new int[] { R.id.transferredtoName, R.id.transferredStatus, R.id.date });
                            // updating listview
                            setListAdapter(adapter);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AllHistoryDetails", "All History Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {
            @Override

            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonParam = new JSONObject();

                try {
                    jsonParam.put("username", phoneNumber);
                    jsonParam.put("Index", index);
                    jsonParam.put("transactid", transactionId);

                    //jsonParam.put("longitude", 0D);
                    Log.d("AllHistoryDetails JSON", jsonParam.toString());
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


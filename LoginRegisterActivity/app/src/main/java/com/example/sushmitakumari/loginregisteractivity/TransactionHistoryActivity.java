package com.example.sushmitakumari.loginregisteractivity;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class TransactionHistoryActivity extends TabActivity {

    private static final String ALL_HISTORY = "All";
    private static final String TRANSFERRED_HISTORY = "Transferred";
    private static final String RECEIVED_HISTORY = "Received";

    String number = null;
    String name = null;
    Float amount = (float)0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("username");
        number = bundle.getString("phonenumber");
        amount = bundle.getFloat("payerbalance");

        TabHost tabHost = getTabHost();
        //For AllHistoryDetails
        TabHost.TabSpec allSpec = tabHost.newTabSpec(ALL_HISTORY);
        allSpec.setIndicator("All");
        Intent allIntent = new Intent(this, AllHistoryDetails.class);
        allIntent.putExtra("phonenumber", number);
        allIntent.putExtra("username", name);
        allIntent.putExtra("payerbalance", amount);
        allSpec.setContent(allIntent);

        //For ReceivedDetails
        TabHost.TabSpec receivedSpec = tabHost.newTabSpec(RECEIVED_HISTORY);
        receivedSpec.setIndicator("Received");
        Intent receivedIntent = new Intent(this, ReceivedActivity.class);
        receivedSpec.setContent(receivedIntent);

        //For TransferredDetails
        TabHost.TabSpec transferredSpec = tabHost.newTabSpec(TRANSFERRED_HISTORY);
        transferredSpec.setIndicator("Transferred");
        Intent transferredIntent = new Intent(this, TransferredActivity.class);
        transferredSpec.setContent(transferredIntent);

        tabHost.addTab(allSpec); // Adding Inbox tab
        tabHost.addTab(transferredSpec); // Adding Outbox tab
        tabHost.addTab(receivedSpec); // Adding Profile tab
    }
}
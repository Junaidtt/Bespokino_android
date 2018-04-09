package com.app.bespokino.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.app.bespokino.R;
import com.app.bespokino.helper.SQLiteHandler;
import com.app.bespokino.helper.SessionManager;

public class NewsizeActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private SessionManager session;


    Button sendMeasuringToolButton,useSmartSizeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_size);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sendMeasuringToolButton = (Button)findViewById(R.id.sendMeasuringToolButton);
        useSmartSizeButton=(Button)findViewById(R.id.useSmartSizeButton);

        sendMeasuringToolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewsizeActivity.this,DepositActivity.class);
                startActivity(intent);


            }
        });

        useSmartSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewsizeActivity.this,CreateStanderdSizeActivity.class);
                startActivity(intent);



            }
        });
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(NewsizeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

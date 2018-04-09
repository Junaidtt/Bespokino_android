package com.app.bespokino.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.bespokino.R;

public class CreateStanderdSizeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Button continueButton;
    ImageButton thin,normal,medium,largee;

    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_smartsize);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        thin = (ImageButton)findViewById(R.id.thin);
        normal = (ImageButton)findViewById(R.id.normal);
        medium = (ImageButton)findViewById(R.id.medium);
        largee = (ImageButton)findViewById(R.id.large);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Spinner element
        Spinner spinnerHeight = (Spinner) findViewById(R.id.spinner1);
        Spinner spinnerWeight = (Spinner) findViewById(R.id.spinner2);
        Spinner spinnershirtSize = (Spinner) findViewById(R.id.spinner3);
        Spinner spinnershirtFit = (Spinner) findViewById(R.id.spinner4);
        Spinner spinnerPantWaist = (Spinner) findViewById(R.id.spinner5);

        //Button
        continueButton = (Button)findViewById(R.id.btContinue);
        linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout)findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout)findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout)findViewById(R.id.linearLayout4);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout1.setSelected(true);

            }
        });

        // Spinner click listener
        spinnerHeight.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.height,R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapterWeight=ArrayAdapter.createFromResource(this, R.array.weight, R.layout.spinner_item);
        ArrayAdapter<CharSequence> shirtSizeAdapter=ArrayAdapter.createFromResource(this, R.array.shirtsize,R.layout.spinner_item);
        ArrayAdapter<CharSequence> shirtFitAdapter=ArrayAdapter.createFromResource(this, R.array.shirtfit, R.layout.spinner_item);
        ArrayAdapter<CharSequence> pantWaistAdapter=ArrayAdapter.createFromResource(this, R.array.pantwaistsize, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        shirtSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shirtFitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pantWaistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerHeight.setAdapter(adapter);
        spinnerWeight.setAdapter(adapterWeight);

        spinnershirtSize.setAdapter(shirtSizeAdapter);
        spinnershirtFit.setAdapter(shirtFitAdapter);
        spinnerPantWaist.setAdapter(pantWaistAdapter);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CreateStanderdSizeActivity.this,SizeAdjustentActivity.class);
                startActivity(intent);
                finish();

            }
        });

        thin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                thin.setBackgroundResource(R.drawable.shape);
                normal.setBackgroundResource(0);
                medium.setBackgroundResource(0);
                largee.setBackgroundResource(0);
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                normal.setBackgroundResource(R.drawable.shape);
                thin.setBackgroundResource(0);
                medium.setBackgroundResource(0);
                largee.setBackgroundResource(0);

            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                medium.setBackgroundResource(R.drawable.shape);
                normal.setBackgroundResource(0);
                thin.setBackgroundResource(0);
                largee.setBackgroundResource(0);
            }
        });

        largee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                largee.setBackgroundResource(R.drawable.shape);
                normal.setBackgroundResource(0);
                thin.setBackgroundResource(0);
                medium.setBackgroundResource(0);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String item=parent.getItemAtPosition(position).toString();
        if(!item.equalsIgnoreCase("HEIGHT")) {

            Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
            String value = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

package com.app.bespokino.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.bespokino.R;

public class SizeAdjustentActivity extends AppCompatActivity {

    float sizeNeck = 17.7f;
    float shoulder = 13.5f;
    float chest = 49.9f;
    float waist = 37f;
    float sleeve = 34.41f;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_adjustent);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        saveButton =(Button)findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(SizeAdjustentActivity.this,CartActivity.class);
                startActivity(intent);
                finish();


            }
        });
    }

    public void onPlus(View view) {
        sizeNeck = (float) (sizeNeck + 0.1f);
        display(sizeNeck);

    }public void onMinus(View view) {
        sizeNeck = (float) (sizeNeck - 0.1f);
        display(sizeNeck);
    }

    private void display(float number) {
        EditText displayInteger = (EditText) findViewById(
                R.id.edSize);

        displayInteger.setText("" + String.format("%.1f",number));
    }

    //Shoulder

    public void onShoulderPlus(View view) {
        shoulder = (float) (shoulder + 0.1f);
        displayShoulder(shoulder);

    }public void onShoulderMinus(View view) {
        shoulder = (float) (shoulder - 0.1f);
        displayShoulder(shoulder);
    }

    private void displayShoulder(float number) {
        EditText shoulder = (EditText) findViewById(
                R.id.edShoulder);

        shoulder.setText("" + String.format("%.1f",number));
    }

    //chest

    public void onChestPlus(View view) {
        chest = (float) (chest + 0.1f);
        displayChest(chest);

    }public void onChestMinus(View view) {
        chest = (float) (chest - 0.1f);
        displayChest(chest);
    }

    private void displayChest(float number) {
        EditText chest = (EditText) findViewById(
                R.id.edChest);

        chest.setText("" + String.format("%.1f",number));
    }

    //waist

    public void onWaistPlus(View view) {
        waist = (float) (waist + 0.1f);
        displayWaist(waist);

    }public void onWaistMinus(View view) {
        waist = (float) (waist - 0.1f);
        displayWaist(waist);
    }

    private void displayWaist(float number) {
        EditText waist = (EditText) findViewById(
                R.id.edWaist);

        waist.setText("" + String.format("%.1f",number));
    }


    //Sleeve
    public void onSleevePlus(View view) {
        sleeve = (float) (sleeve + 0.1f);
        displaySleeve(sleeve);

    }public void onSleeveMinus(View view) {
        sleeve = (float) (sleeve - 0.1f);
        displaySleeve(sleeve);
    }

    private void displaySleeve(float number) {
        EditText sleeve = (EditText) findViewById(
                R.id.edSleeve);

        sleeve.setText("" + String.format("%.1f",number));
    }

}

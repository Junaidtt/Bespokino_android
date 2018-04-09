package com.app.bespokino.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.bespokino.R;

public class MeasuringToolStatus extends AppCompatActivity {

    ImageButton btSendMetool,btToolPresent;
    ImageView ticker,tick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measuring_tool_status);

        btSendMetool = (ImageButton)findViewById(R.id.btSend);
        btToolPresent = (ImageButton)findViewById(R.id.btPresnt);

        tick = (ImageView) findViewById(R.id.tick);
        ticker = (ImageView) findViewById(R.id.ticker);

        tick.setVisibility(View.INVISIBLE);
        ticker.setVisibility(View.INVISIBLE);


        btSendMetool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tick.setVisibility(View.VISIBLE);
                ticker.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MeasuringToolStatus.this,Pay20cardActivity.class);
                //intent.putExtra("uid",uID);
                startActivity(intent);
            }
        });



        btToolPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tick.setVisibility(View.INVISIBLE);
                ticker.setVisibility(View.VISIBLE);

                Intent intent = new Intent(MeasuringToolStatus.this, GetMeasurmentActivity.class);
                startActivity(intent);
            }
        });

    }
}

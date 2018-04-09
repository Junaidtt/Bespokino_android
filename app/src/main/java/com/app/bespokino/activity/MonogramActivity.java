package com.app.bespokino.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.ItemAdapter;
import com.app.bespokino.adapter.ItemBigAdapter;
import com.app.bespokino.fragment.MonoColorDialogFragment;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class MonogramActivity extends AppCompatActivity {

    TextView textCartItemCount;
    int mCartItemCount = 2;

    int preSelectedIndex = -1;
    boolean exist = false;

    ItemModel model;

    Bundle bundle;

    Button lockyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monogram);

        lockyButton = (Button)findViewById(R.id.iamOnLockey);
        bundle = new Bundle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        GridView gridView = (GridView)findViewById(R.id.gridview);

        final FragmentManager fm = getFragmentManager();
        final MonoColorDialogFragment p = new MonoColorDialogFragment();

        final List<ItemModel> item = new ArrayList<>();

        //item.add(new ItemModel(false,"NO MONOGRAM",R.drawable.no_monogram,"MONO"));
        item.add(new ItemModel(false,"POCKET",R.drawable.monogram_pocket,"MONO",172,48));
        item.add(new ItemModel(false,"CUFF",R.drawable.monogram_cuff,"MONO",171,48));
        item.add(new ItemModel(false,"BACK OF COLLAR",R.drawable.monogram_back_collar,"MONO",174,48));
        item.add(new ItemModel(false,"INSIDE COLLAR",R.drawable.monogram_inside_collar,"MONO",173,48));
        item.add(new ItemModel(false,"BODY",R.drawable.monogram_body,"MONO",170,48));

        final ItemBigAdapter adapter = new ItemBigAdapter(this, item);

        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                model = item.get(position); //changed it to model because viewers will confused about it

                model.setSelected(true);


                item.set(position, model);

                if (preSelectedIndex > -1){

                    ItemModel preRecord = item.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    item.set(preSelectedIndex, preRecord);

                }

                preSelectedIndex = position;


                adapter.updateRecords(item);

                int m = model.getOptionValude();

                bundle.putInt("mgPosition",m);

                p.setArguments(bundle);

                p.show(fm,"player");

            }
        });

        lockyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MonogramActivity.this,AdditionaloptionsActivity.class));

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_cart:
                Toast.makeText(this, "You have selected cart Menu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MonogramActivity.this,CartActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  void setupBadge() {

        if (textCartItemCount != null) {
            if (AppConfig.mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(AppConfig.mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}

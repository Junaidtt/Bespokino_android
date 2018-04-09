package com.app.bespokino.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.fragment.BicepsDialogFragment;
import com.app.bespokino.fragment.ChestDialogFragment;
import com.app.bespokino.fragment.CuffDialogFragment;
import com.app.bespokino.fragment.HipsDialogFragment;
import com.app.bespokino.fragment.LengthDialogFragment;
import com.app.bespokino.fragment.NeckDialogFragment;
import com.app.bespokino.fragment.ShoulderDialogFragment;
import com.app.bespokino.fragment.SizeChooserDialogFragment;
import com.app.bespokino.fragment.SleeveDialogFragment;
import com.app.bespokino.fragment.WaistDialogFragment;
import com.app.bespokino.helper.AppConfig;
import com.badoualy.stepperindicator.StepperIndicator;

public class GetMeasurmentActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    Button Next,back;
    TextView textCartItemCount;
     ViewPager vpPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_measurment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());


        final StepperIndicator indicator = (StepperIndicator) findViewById(R.id.stepper_indicator);


        vpPager.setAdapter(adapterViewPager);
        indicator.setViewPager(vpPager);
        indicator.setViewPager(vpPager, vpPager.getAdapter().getCount() - 1); //
        indicator.setStepCount(10);
        indicator.setCurrentStep(0);
        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                vpPager.setCurrentItem(step, true);
            }
        });



    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 10;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                   // return FirstFragment.newInstance(0, "Page # 1");
                    return new SizeChooserDialogFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                  //  return FirstFragment.newInstance(1, "Page # 2");
                    return new ShoulderDialogFragment();

                case 2: // Fragment # 1 - This will show SecondFragment
                   // return SecondFragment.newInstance(2, "Page # 3");
                    return new SleeveDialogFragment();

                case 3: // Fragment # 1 - This will show SecondFragment
                    // return SecondFragment.newInstance(2, "Page # 3");
                    return new ChestDialogFragment();

                case 4: // Fragment # 1 - This will show SecondFragment
                    // return SecondFragment.newInstance(2, "Page # 3");
                    return new WaistDialogFragment();
                case 5: // Fragment # 1 - This will show SecondFragment
                    // return SecondFragment.newInstance(2, "Page # 3");
                    return new HipsDialogFragment();
                case 6: // Fragment # 1 - This will show SecondFragment
                    // return SecondFragment.newInstance(2, "Page # 3");
                    return new BicepsDialogFragment();
                case 7: // Fragment # 1 - This will show SecondFragment
                    // return SecondFragment.newInstance(2, "Page # 3");
                    return new NeckDialogFragment();
                case 8: // Fragment # 1 - This will show SecondFragment
                    // return SecondFragment.newInstance(2, "Page # 3");
                    return new CuffDialogFragment();
                case 9: // Fragment # 1 - This will show SecondFragment
                    // return SecondFragment.newInstance(2, "Page # 3");
                    return new LengthDialogFragment();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        vpPager.setCurrentItem(item, smoothScroll);
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
                Intent intent = new Intent(GetMeasurmentActivity.this,CartActivity.class);
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

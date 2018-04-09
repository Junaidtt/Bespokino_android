package com.app.bespokino.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.app.bespokino.R;
import com.app.bespokino.adapter.TabAdapter;
import com.rahimlis.badgedtablayout.BadgedTabLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {


    public TabLayout tabLayout;
    public ViewPager viewPager;
    public  static int int_items= 3;



    int NUM_PAGES = 3;
    int currentPage = 0;
    boolean touched = false;
    Handler handler = new Handler();
    Runnable update;
    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab,null);
       // tabLayout=(TabLayout)v.findViewById(R.id.tabs);
        final BadgedTabLayout tabLayout = (BadgedTabLayout)v.findViewById(R.id.tabs);
       // tabLayout.setupWithViewPager(mViewPager);
        viewPager=(ViewPager)v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabAdapter( getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setBadgeText(0,"$79");
                tabLayout.setBadgeText(1,"+$25");
                tabLayout.setBadgeText(2,"+$40");
            }
        });


        tabLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        touched = true;
                        return true;

                    case MotionEvent.ACTION_UP:
                        touched = false;
                        return true;
                }

                return false;
            }
        });


        startPagerAutoSwipe();


        return v;


    }
    private void startPagerAutoSwipe() {
        update = new Runnable() {
            public void run() {
                if(!touched){
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    viewPager.setCurrentItem(currentPage++, true);
                }
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 5000, 5000);
    }


}

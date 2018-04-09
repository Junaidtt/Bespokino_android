package com.app.bespokino.adapter;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.bespokino.fragment.Included;
import com.app.bespokino.fragment.IncludedFragment;
import com.app.bespokino.fragment.Luxury;
import com.app.bespokino.fragment.LuxuryFragment;
import com.app.bespokino.fragment.Premium;
import com.app.bespokino.fragment.PremiumFragment;

import static com.app.bespokino.fragment.TabFragment.int_items;

/**
 * Created by sunilvg on 04/07/17.
 */

public class TabAdapter extends FragmentPagerAdapter {



    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new Included();
            case 1:
                return new Premium();
            case 2:
                return new Luxury();

        }

        return null;
    }

    @Override
    public int getCount() {
        return int_items;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Shirt";
            case 1:
                return "Premium";
            case 2:
                return "Luxury";

        }

        return null;
    }


}

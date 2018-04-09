package com.app.bespokino.adapter;

/**
 * Created by bespokino on 9/11/2017 AD.
 */



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.bespokino.fragment.BillingFragment;
import com.app.bespokino.fragment.PaymentFragment;
import com.app.bespokino.fragment.ShippingFragment;
import com.app.bespokino.helper.ValidateFragment;

public class CheckoutPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CheckoutPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public int getItemPosition(Object object) {

        if (object instanceof ValidateFragment) {
            ((ValidateFragment) object).validate();
        }
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BillingFragment tab1 = new BillingFragment();
                return tab1;
            case 1:

                ShippingFragment tab2 = new ShippingFragment();
                return tab2;
            case 2:
                PaymentFragment tab3 = new PaymentFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }




}
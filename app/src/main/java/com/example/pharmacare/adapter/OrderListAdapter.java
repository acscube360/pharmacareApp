package com.example.pharmacare.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.pharmacare.fragment.ActiveOrderFragment;
import com.example.pharmacare.fragment.CompleteOrderFragment;
import com.example.pharmacare.model.IOrderDetailsSearch;

public class OrderListAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    CompleteOrderFragment completeOrderFragment;
    ActiveOrderFragment activeOrderFragment;

    IOrderDetailsSearch completeIOrderDetailsSearch;
    IOrderDetailsSearch activeIOrderDetailsSearch;

    public OrderListAdapter(Context context, @NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return completeOrderFragment;

            case 1:
                return activeOrderFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public IOrderDetailsSearch getOrderDetailsSearchListener(int position) {

        if (position == 0) {
            return completeIOrderDetailsSearch;
        } else if (position == 1) {
            return activeIOrderDetailsSearch;
        }
        return null;
    }
}

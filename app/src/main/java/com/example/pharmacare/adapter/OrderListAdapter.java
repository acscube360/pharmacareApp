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

    private IOrderDetailsSearch completeOrderSearch;
    private IOrderDetailsSearch activeOrderSearch;

    public OrderListAdapter(Context context, @NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        setupFragments();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                completeOrderFragment = new CompleteOrderFragment();
                return completeOrderFragment;

            case 1:
//                activeOrderFragment = new ActiveOrderFragment();
                return activeOrderFragment;

            default:
                return null;
        }

    }

    public IOrderDetailsSearch getOrderDetailsSearchListener(int position) {

        if (position == 0) {
            return completeOrderSearch;
        } else if (position == 1) {
            return activeOrderSearch;
        }
        return null;
    }

    private void setupFragments() {
        completeOrderFragment = new CompleteOrderFragment();
        completeOrderSearch = new IOrderDetailsSearch() {
            @Override
            public void onSearch(String inputString) {
                completeOrderFragment.filterTransactions(inputString);
            }
        };

        activeOrderFragment = new ActiveOrderFragment();
        activeOrderSearch = new IOrderDetailsSearch() {
            @Override
            public void onSearch(String inputString) {
                activeOrderFragment.filterTransactions(inputString);
            }
        };

    }

    @Override
    public int getCount() {
        return totalTabs;
    }

}

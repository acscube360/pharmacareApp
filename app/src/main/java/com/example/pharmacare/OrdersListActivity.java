package com.example.pharmacare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pharmacare.adapter.OrderListAdapter;
import com.example.pharmacare.fragment.ActiveOrderFragment;
import com.example.pharmacare.fragment.CompleteOrderFragment;
import com.example.pharmacare.model.IOrderDetailsSearch;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrdersListActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_top_filter;
    private FrameLayout simpleFrameLayout;

    private int tabPosition = 0;
    private ViewPager viewpager;
    private TabLayout tabLayout;
    private EditText et_remark;
    private IOrderDetailsSearch orderDetailsSearchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        initView();
    }

    private void initView() {
        iv_top_filter = findViewById(R.id.iv_top_filter);
        et_remark = findViewById(R.id.et_remark);
        iv_top_filter.setVisibility(View.VISIBLE);
        iv_top_filter.setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Complete Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Active Order"));
//        tabLayout.addTab(tabLayout.newTab().setText("Favourite"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
//        adapter.addFragment(new CompleteOrderFragment(), "Complete Order", 0);
//        adapter.addFragment(new ActiveOrderFragment(), "Active Order", 1);
//        orderDetailsSearchListener = adapter.getOrderDetailsSearchListener(0);
//        viewpager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewpager);

        OrderListAdapter adapter = new OrderListAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        orderDetailsSearchListener = adapter.getOrderDetailsSearchListener(0);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

//


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                orderDetailsSearchListener = adapter.getOrderDetailsSearchListener(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        et_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(OrdersListActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                orderDetailsSearchListener.onSearch(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    private class ViewPagerAdapter extends FragmentPagerAdapter implements IOrderDetailsSearch {
//        private List<Fragment> fragments = new ArrayList<>();
//        private List<String> fragmentTitles = new ArrayList<>();
//        private IOrderDetailsSearch iOrderDetailsSearch;
//        private IOrderDetailsSearch iActiveOrderDetailsSearch;
//
//        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
//            super(fm, behavior);
//        }
//
//        //add fragment to the viewpager
//        public void addFragment(Fragment fragment, String title, int position) {
//
//            fragments.add(fragment);
//            fragmentTitles.add(title);
////            switch (position) {
////                case 0:
////            iOrderDetailsSearch = new IOrderDetailsSearch() {
////                @Override
////                public void onSearch(String inputString) {
////                    new CompleteOrderFragment().onSearch(inputString);
////                }
////            };
////            }
//        }
//
//        public IOrderDetailsSearch getOrderDetailsSearchListener(int position) {
//
//            if (position == 0) {
//                return iOrderDetailsSearch;
//            } else if (position == 1) {
//                return iActiveOrderDetailsSearch;
//            }
//            return null;
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            return fragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.size();
//        }
//
//        //to setup title of the tab layout
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return fragmentTitles.get(position);
//        }
//
//        @Override
//        public void onSearch(String inputString) {
//            iOrderDetailsSearch.onSearch(inputString);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_filter:
                Toast.makeText(this, "filter", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
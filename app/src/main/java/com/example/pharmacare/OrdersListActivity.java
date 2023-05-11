package com.example.pharmacare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
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
                et_remark.setText("");
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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_filter:
                showCustomDialog();
                break;
        }
    }

    private void showCustomDialog() {
//        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(OrdersListActivity.this).inflate(R.layout.layout_filter_dialog, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(OrdersListActivity.this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        TextView tv_confirm_message = dialogView.findViewById(R.id.tv_confirm_message);
//        tv_confirm_message.setText("Are you Sure to Submit this order ?");
//        AppCompatButton btn_confirm_dialog = dialogView.findViewById(R.id.btn_confirm_dialog);
//        btn_confirm_dialog.setBackgroundResource(R.drawable.btn_next_background);

        dialogView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

//        btn_confirm_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToHome();
//            }
//        });
        alertDialog.show();
    }
}
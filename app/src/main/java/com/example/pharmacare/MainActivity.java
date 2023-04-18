package com.example.pharmacare;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_new_orders,btn_orders;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_new_orders=findViewById(R.id.btn_new_orders);
        btn_orders=findViewById(R.id.btn_orders);
        btn_new_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: btn_new_orders");
                Intent intent=new Intent(new Intent(v.getContext(),AddNewOrderActivity.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        btn_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: btn_new_orders");
                Intent intent=new Intent(new Intent(v.getContext(),OrdersListActivity.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        // add comment
    }

    @Override
    public void onClick(View v) {

    }
}
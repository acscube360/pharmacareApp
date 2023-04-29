package com.example.pharmacare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pharmacare.model.Order;

public class ShowOrderDetailsActivity extends AppCompatActivity {

    private ImageView iv_top_back;
    private TextView tv_top_title;
    private RecyclerView rv_show_order_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_details);
        Intent intent=getIntent();
        Order order=new Order();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            order=(Order) intent.getSerializableExtra("order");
//        }

        initView(order);
    }

    private void initView(Order order) {
        iv_top_back=findViewById(R.id.iv_top_back);
        iv_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_top_title=findViewById(R.id.tv_top_title);
        tv_top_title.setText(order.getRemark());
        rv_show_order_items=findViewById(R.id.rv_show_order_items);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);

    }
}
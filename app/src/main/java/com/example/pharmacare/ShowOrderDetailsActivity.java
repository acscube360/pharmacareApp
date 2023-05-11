package com.example.pharmacare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacare.adapter.OrderItemListAdapter;
import com.example.pharmacare.model.Order;
import com.example.pharmacare.model.OrderBatchItem;
import com.example.pharmacare.model.OrderItem;
import com.example.pharmacare.utility.Api;
import com.example.pharmacare.utility.CheckNetwork;
import com.example.pharmacare.utility.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowOrderDetailsActivity extends AppCompatActivity {

    private ImageView iv_top_back;
    private TextView tv_top_title;
    private RecyclerView rv_show_order_items;
    private ArrayList<OrderBatchItem> orderBatchItems;
    private ArrayList<OrderItem> orderItemArrayList;
    private static OrderItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_details);
        Intent intent = getIntent();
        Order order = new Order();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        order = (Order) intent.getSerializableExtra("order");
//        }
        orderBatchItems = new ArrayList<>();
        orderItemArrayList = new ArrayList<>();
        initView(order);
    }

    private void initView(Order order) {

        iv_top_back = findViewById(R.id.iv_top_back);
        iv_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_top_title = findViewById(R.id.tv_top_title);
        tv_top_title.setText(order.getRemark());
        rv_show_order_items = findViewById(R.id.rv_show_order_items);

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
            //getOrderBatchItems(order);
            getItems(order);
        } else {
            Toast.makeText(ShowOrderDetailsActivity.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);

    }

    private void getItems(Order order) {
        String url = Api.BASE_URL + "oderBatchItems?$filter=orderId eq " + order.getId() + "&$select=totalPrice,unitPrice,totalCost,quantity,id&$expand=itemBatch($expand=item($select=name,id))&$expand=sellingType($select=name)&$expand=itemBatch($select=item)";
        RetrofitClient.getInstance().getMyApi().getItemListOfOrder(url).enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                Log.e("request", call.request().url().toString());
                Log.e("response", response.raw().toString());
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        orderItemArrayList.add(response.body().get(i));
                        orderItemArrayList.get(i).setName(orderItemArrayList.get(i).getItemBatch().getItem().getName());
//                        Log.e("item name", orderItemArrayList.get(i).getItemBatch().getItem().getName());
//                        Log.e("sell type", orderItemArrayList.get(i).getSellingType().getName());
//                        Log.e("quantity", String.valueOf(orderItemArrayList.get(i).getQuantity()));
                    }
                    adapter = new OrderItemListAdapter(ShowOrderDetailsActivity.this, orderItemArrayList);
                    rv_show_order_items.setLayoutManager(new LinearLayoutManager(ShowOrderDetailsActivity.this));
                    rv_show_order_items.setAdapter(adapter);
                }else{
                    Toast.makeText(ShowOrderDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ShowOrderDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOrderBatchItems(Order order) {

        final ProgressDialog progressDialog = new ProgressDialog(ShowOrderDetailsActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Fetching Order Details...");
        // set message
        progressDialog.show();

        RetrofitClient.getInstance().getMyApi().getOrderBatchItems().enqueue(new Callback<List<OrderBatchItem>>() {
            @Override
            public void onResponse(Call<List<OrderBatchItem>> call, Response<List<OrderBatchItem>> response) {
                Log.e("response", response.body().toString());
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getOrderId() == order.getId()) {
                            orderBatchItems.add(response.body().get(i));
//                            Log.e("added", ((Str) response.body().get(i).getItemBatchId()));
                        }
                    }
                    Log.e("batchItems", String.valueOf(orderBatchItems.size()));
                } else {
                    Toast.makeText(ShowOrderDetailsActivity.this, "Error when fetching order details", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<OrderBatchItem>> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(ShowOrderDetailsActivity.this, "Error fetching order details", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
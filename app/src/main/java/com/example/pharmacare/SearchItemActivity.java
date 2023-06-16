package com.example.pharmacare;

import static androidx.camera.core.CameraX.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacare.model.Item;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.utility.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchItemActivity extends AppCompatActivity {

    private ArrayList<Item> itemArrayList;
    private ArrayList<ItemBatch> itemBatchArrayList = new ArrayList<>();
    private ArrayList<String> itemNamesArraylist;
    private AutoCompleteTextView auto_tv_search;
    private Button btn_add_order;
    private TextView tv_id_item, tv_item_name, tv_item_barcode, tv_item_measurement, tv_item_count;
    private int available_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);
        tv_id_item = findViewById(R.id.tv_id_item);
        tv_item_name = findViewById(R.id.tv_item_name);
        tv_item_barcode = findViewById(R.id.tv_item_barcode);
        tv_item_measurement = findViewById(R.id.tv_item_measurement);
        tv_item_count = findViewById(R.id.tv_item_count);
//        auto_tv_search=findViewById(R.id.auto_tv_search);
        itemBatchArrayList = (ArrayList<ItemBatch>) getIntent().getSerializableExtra("itemBatches");
        initView(itemBatchArrayList);

//        loadItems();

    }


    private void loadItems() {
        final ProgressDialog progressDialog = new ProgressDialog(SearchItemActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Fetching data...."); // set message
        progressDialog.show();
        itemArrayList = new ArrayList<>();
        itemNamesArraylist = new ArrayList<>();
        RetrofitClient.getInstance().getMyApi().getAllItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().size(); i++) {
                        itemArrayList.add(response.body().get(i));
                        itemNamesArraylist.add(response.body().get(i).getName());
                    }
                    Log.e("itemListSize>>>>>>>", String.valueOf(itemArrayList.size()));
                    Log.e("itemNameSize>>>>>>>", String.valueOf(itemNamesArraylist.size()));
                    ArrayAdapter adapter = new ArrayAdapter
                            (getApplicationContext(), android.R.layout.select_dialog_item, itemNamesArraylist);
                    auto_tv_search.setThreshold(1);
                    auto_tv_search.setAdapter(adapter);

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(SearchItemActivity.this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddItemDialog() {
        ImageView iv_close;
        Button btn_cancel, btn_add;

        View dialogView = LayoutInflater.from(SearchItemActivity.this).inflate(R.layout.add_new_order__in_search_view, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchItemActivity.this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        iv_close = dialogView.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                popupWindow.dismiss();
                alertDialog.dismiss();
            }
        });
        btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        btn_add = dialogView.findViewById(R.id.btn_add);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popupWindow.dismiss();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void initView(ArrayList<ItemBatch> itemBatches) {



        available_count = 0;

        for (ItemBatch itemBatch : itemBatches) {
            available_count += itemBatch.getStock();
        }


        tv_item_name.setText(itemBatches.get(0).getItem().getName());
        tv_item_barcode.setText(itemBatches.get(0).getItem().getBarcode());
        tv_item_measurement.setText(itemBatches.get(0).getItem().getMeasurement());
//        tv_item_count.setText(available_count);
        tv_id_item.setText(itemBatches.get(0).getId());
        btn_add_order = findViewById(R.id.btn_add_order);
        btn_add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog();
            }
        });
    }

}
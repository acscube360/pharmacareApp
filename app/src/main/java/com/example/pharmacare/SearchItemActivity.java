package com.example.pharmacare;

import static androidx.camera.core.CameraX.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacare.adapter.ItemBatchListAdapter;
import com.example.pharmacare.model.Item;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.model.Supplier;
import com.example.pharmacare.utility.CheckNetwork;
import com.example.pharmacare.utility.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchItemActivity extends AppCompatActivity {

    private ArrayList<Supplier> supplierArrayList;
    private ArrayList<Item> itemArrayList;
    private ArrayList<ItemBatch> itemBatchArrayList = new ArrayList<>();
    private ArrayList<String> itemNamesArraylist;
    private ArrayList<String> supplierNameslist;
    private AutoCompleteTextView auto_tv_search;
    private Button btn_add_order, btn_view_more;
    private TextView tv_id_item, tv_item_name, tv_item_barcode, tv_item_measurement, tv_item_count, tv_top_title;
    private int available_count;
    private RecyclerView rv_view_more;
    private ItemBatchListAdapter batchListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);

//        auto_tv_search=findViewById(R.id.auto_tv_search);
        supplierNameslist = new ArrayList<>();
        supplierArrayList = new ArrayList<>();
        supplierNameslist.add("Select Supplier");
        itemBatchArrayList = (ArrayList<ItemBatch>) getIntent().getSerializableExtra("itemBatches");

        if (CheckNetwork.isInternetAvailable(SearchItemActivity.this)){
            loadSuppliers();
        }else{
            Toast.makeText(this, "Turn on your internet Connection", Toast.LENGTH_SHORT).show();
        }
        initView(itemBatchArrayList);


    }


//    private void loadItems() {
//        final ProgressDialog progressDialog = new ProgressDialog(SearchItemActivity.this);
//        progressDialog.setCancelable(false); // set cancelable to false
//        progressDialog.setMessage("Fetching data...."); // set message
//        progressDialog.show();
//        itemArrayList = new ArrayList<>();
//        itemNamesArraylist = new ArrayList<>();
//        RetrofitClient.getInstance().getMyApi().getAllItems().enqueue(new Callback<List<Item>>() {
//            @Override
//            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
//                if (response.isSuccessful()) {
//
//                    for (int i = 0; i < response.body().size(); i++) {
//                        itemArrayList.add(response.body().get(i));
//                        itemNamesArraylist.add(response.body().get(i).getName());
//                    }
//                    Log.e("itemListSize>>>>>>>", String.valueOf(itemArrayList.size()));
//                    Log.e("itemNameSize>>>>>>>", String.valueOf(itemNamesArraylist.size()));
//                    ArrayAdapter adapter = new ArrayAdapter
//                            (getApplicationContext(), android.R.layout.select_dialog_item, itemNamesArraylist);
//                    auto_tv_search.setThreshold(1);
//                    auto_tv_search.setAdapter(adapter);
//
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Item>> call, Throwable t) {
//                progressDialog.dismiss();
//                t.printStackTrace();
//                Toast.makeText(SearchItemActivity.this, "Server Connection Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void showAddItemDialog(ArrayList<Supplier> supplierArrayList,ArrayList<String> supplierNames,String itemName) {
        ImageView iv_close;
        TextView titleText;
        Button btn_cancel, btn_add;
        Spinner supplier_spinner;

        View dialogView = LayoutInflater.from(SearchItemActivity.this).inflate(R.layout.add_new_order__in_search_view, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchItemActivity.this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        iv_close = dialogView.findViewById(R.id.iv_close);
        titleText = dialogView.findViewById(R.id.titleText);
        titleText.setText("Add New Order to "+itemName);
        btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        btn_add = dialogView.findViewById(R.id.btn_add);
        supplier_spinner = dialogView.findViewById(R.id.supplier_spinner);



        ArrayAdapter supAdapter = new ArrayAdapter(SearchItemActivity.this, R.layout.spinner_item, supplierNames);
        supplier_spinner.setAdapter(supAdapter);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                popupWindow.dismiss();
                alertDialog.dismiss();
            }
        });
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
        rv_view_more = findViewById(R.id.rv_view_more);
        tv_id_item = findViewById(R.id.tv_id_item);
        tv_item_name = findViewById(R.id.tv_item_name);
        tv_item_barcode = findViewById(R.id.tv_item_barcode);
        tv_item_measurement = findViewById(R.id.tv_item_measurement);
        tv_top_title = findViewById(R.id.tv_top_title);


        tv_item_count = findViewById(R.id.tv_item_count);
        btn_view_more = findViewById(R.id.btn_view_more);
        tv_top_title.setText(itemBatches.get(0).getItem().getName());

        available_count = 0;

        for (ItemBatch itemBatch : itemBatches) {
            available_count += itemBatch.getStock();
        }


        tv_item_name.setText(itemBatches.get(0).getItem().getName());
        tv_item_barcode.setText(itemBatches.get(0).getItem().getBarcode());
        tv_item_measurement.setText(itemBatches.get(0).getItem().getMeasurement());
//        tv_item_count.setText(available_count);
        tv_id_item.setText(String.valueOf(itemBatches.get(0).getId()));
        btn_add_order = findViewById(R.id.btn_add_order);


        batchListAdapter = new ItemBatchListAdapter(itemBatchArrayList, SearchItemActivity.this);
        rv_view_more.setLayoutManager(new LinearLayoutManager(SearchItemActivity.this));
        rv_view_more.setAdapter(batchListAdapter);

        rv_view_more.setVisibility(View.GONE);


        btn_add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog(supplierArrayList,supplierNameslist,itemBatches.get(0).getItem().getName());
            }
        });
        btn_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Transition transition = new Fade();
////                Transition transition = new Slide(Gravity.BOTTOM);
//                transition.setDuration(600);
//                transition.addTarget(R.id.rv_view_more);

//                TransitionManager.beginDelayedTransition(parent, transition);
                if (rv_view_more.getVisibility() == View.VISIBLE) {
                    rv_view_more.setVisibility(View.GONE);
                } else {
                    rv_view_more.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    private void loadSuppliers() {
        RetrofitClient.getInstance().getMyApi().getAllSuppliers().enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        supplierArrayList.add(response.body().get(i));
                        supplierNameslist.add(response.body().get(i).getName());
                    }
                }else{
                    Toast.makeText(SearchItemActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SearchItemActivity.this, "Error when fetching supplier details!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
package com.example.pharmacare;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacare.model.Item;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.model.ItemSellingType;
import com.example.pharmacare.model.OrderItem;
import com.example.pharmacare.model.SellingType;
import com.example.pharmacare.ui.PopupClass;
import com.example.pharmacare.utility.CheckNetwork;
import com.example.pharmacare.utility.RetrofitClient;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout layout;
    private ImageView iv_open_camera, iv_search;
    private String item_code = "";
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 1000;
    private TextView tv_top_title;
    private EditText et_search;
    private Item item;
    private ArrayList<String> itemBatchnames;
    private ArrayList<SellingType> sellingTypes, itemSellingTypes;
    private ArrayList<OrderItem> orderItemArrayList;
    private boolean isFound = false;
    private static LinearLayout ll_header;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        getSellingTypes();
        initView();

    }

    private void initView() {
        findViewById(R.id.iv_search).setOnClickListener(this);
        item_code = getIntent().getStringExtra("item_code");
        layout = findViewById(R.id.constraint);

        iv_open_camera = findViewById(R.id.iv_open_camera);
        iv_search = findViewById(R.id.iv_search);

        iv_open_camera.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        ll_header = findViewById(R.id.ll_header);

        et_search = findViewById(R.id.et_search);

        tv_top_title = findViewById(R.id.tv_top_title);
        tv_top_title.setText("Add Items");
        item = new Item();
        sellingTypes = new ArrayList<>();
        orderItemArrayList = new ArrayList<>();

        if (orderItemArrayList.size() == 0) {
            ll_header.setVisibility(View.GONE);
        }
        SharedPreferences preferences = getSharedPreferences("Order_items", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ORDER_DATA", "[Aruna]");
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                if (!et_search.getText().toString().isEmpty()) {
                    if (CheckNetwork.isInternetAvailable(v.getContext())) {
                        getItemByNameOrId(et_search.getText().toString(), v);


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isFound) {
                                        getItemBatches(item.getName(), v);
                                }
                            }
                        }, 1000);

                    } else {
                        Toast.makeText(this, "Please Check your internet Connection", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, "Please enter Item name/Id", Toast.LENGTH_SHORT).show();


                }

                break;
            case R.id.iv_open_camera:
                Intent intent = new Intent(v.getContext(), ScanBarcodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("item_code", "");
                startActivity(intent);
        }
    }


    private void getItemByNameOrId(String name, View v) {
        item = new Item();
        final ProgressDialog progressDialog = new ProgressDialog(AddItemActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Searching...."); // set message
        progressDialog.show();
        RetrofitClient.getInstance().getMyApi().getItemByNameOrId(name).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                if (response.isSuccessful()) {

                    Log.e("response>>>", String.valueOf(response.body().size()));
                    Log.e("request>>>", String.valueOf(call.request().url()));
                    progressDialog.dismiss();

                    item = response.body().get(0);
                    isFound = true;
                    Log.e("name", item.getName());
                    Log.e("barcode", item.getBarcode());
                    Log.e("isItemFound", (String.valueOf(isFound)));

                } else {
                    isFound = false;
                    progressDialog.dismiss();
                    Toast.makeText(AddItemActivity.this, "Cannot find requested item ", Toast.LENGTH_SHORT).show();

                }
//

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddItemActivity.this, "Connection Error ", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        });
//        Toast.makeText(AddItemActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
//        getItemBatches(item.getName(),v);


    }

    private void getItemBatches(String name, View v) {
        itemBatchnames = new ArrayList<>();
        Log.e(TAG, "getItemBatches: >>>>>>>>>" + name);
        RetrofitClient.getInstance().getMyApi().getItemBatches(name).enqueue(new Callback<List<ItemBatch>>() {
            @Override
            public void onResponse(Call<List<ItemBatch>> call, Response<List<ItemBatch>> response) {
                Log.e("response>>>>>>>>>", response.raw().toString());
                if (response.isSuccessful()) {
                    itemBatchnames.add("Select the batch");
                    for (int i = 0; i < response.body().size(); i++) {
                        itemBatchnames.add(response.body().get(i).getName());
                        Log.e("add>>", response.body().get(i).toString());
                    }
                    itemSellingTypes = new ArrayList<>();
                    itemSellingTypes.add(new SellingType("Selling type", 0));
                    PopupClass popupClass = new PopupClass();
                    if (item.getItemSellingTypes() != null) {
                        for (ItemSellingType iSt : item.getItemSellingTypes()) {
                            for (SellingType st : sellingTypes) {
                                if (iSt.getId() == st.getId()) {
                                    itemSellingTypes.add(st);
                                }
                            }

                        }
                    }
                    popupClass.showPopupWindow(v, item, itemBatchnames, itemSellingTypes);

                } else {
                    Toast.makeText(AddItemActivity.this, "Cannot find requested item-batch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemBatch>> call, Throwable t) {
                Toast.makeText(AddItemActivity.this, "Connection Error ", Toast.LENGTH_SHORT).show();

            }
        });
        Log.e("batchnames size", String.valueOf(itemBatchnames.size()));
//        getSellingTypes(v);
    }

    private void getSellingTypes() {

        RetrofitClient.getInstance().getMyApi().getAllSellingTypes().enqueue(new Callback<List<SellingType>>() {
            @Override
            public void onResponse(Call<List<SellingType>> call, Response<List<SellingType>> response) {
                if (response.isSuccessful()) {

                    for (int j = 0; j < response.body().size(); j++) {
                        sellingTypes.add(response.body().get(j));
                    }
                   } else {
                    Toast.makeText(AddItemActivity.this, "Connection Error ", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<SellingType>> call, Throwable t) {
                t.printStackTrace();
                Log.e("request>>>>>", call.request().url().toString());
                Toast.makeText(AddItemActivity.this, "Server error ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void showAndUpdateItems(){
        ll_header.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
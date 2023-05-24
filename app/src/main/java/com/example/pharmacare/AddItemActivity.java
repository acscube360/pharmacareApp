package com.example.pharmacare;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacare.adapter.OrderItemListAdapter;
import com.example.pharmacare.model.Item;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.model.ItemSellingType;
import com.example.pharmacare.model.Order;
import com.example.pharmacare.model.OrderItem;
import com.example.pharmacare.model.SellingType;
import com.example.pharmacare.ui.PopupClass;
import com.example.pharmacare.utility.Api;
import com.example.pharmacare.utility.CheckNetwork;
import com.example.pharmacare.utility.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ArrayList<ItemBatch> itemBatches;
    private ArrayList<SellingType> sellingTypes, itemSellingTypes;
    private ArrayList<ItemSellingType>itemSelTypes;
    private ArrayList<OrderItem> orderItemArrayList;
    private boolean isFound = false;
    private static LinearLayout ll_header;
    private static AppCompatButton btn_confirm;
    private static RecyclerView rv_order_items;
    private static OrderItemListAdapter adapter;
    private boolean fromActiveList;
    View v;
    private Order order = new Order();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        v = getWindow().getDecorView().getRootView();
        fromActiveList = getIntent().getBooleanExtra("fromActiveList", false);
        order = (Order) getIntent().getSerializableExtra("order");
        itemBatches = new ArrayList<>();
        itemSelTypes=new ArrayList<>();
        initView();
        if (CheckNetwork.isInternetAvailable(this)) {
            getSellingTypes();
            if (fromActiveList == true) {

                getItems(order);
            } else {


                item_code = getIntent().getStringExtra("item_code");
                if (!item_code.isEmpty()) {

                    getItemByNameOrId(item_code, v);


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isFound) {
                                getItemBatches(item.getName(), v);
                            }
                        }
                    }, 1000);


                }
            }
        } else {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {
        findViewById(R.id.iv_search).setOnClickListener(this);
        item_code = getIntent().getStringExtra("item_code");
        layout = findViewById(R.id.constraint);

        iv_open_camera = findViewById(R.id.iv_open_camera);
        btn_confirm = findViewById(R.id.btn_confirm);
        iv_search = findViewById(R.id.iv_search);

        iv_open_camera.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        ll_header = findViewById(R.id.ll_header);

        et_search = findViewById(R.id.et_search);
        rv_order_items = findViewById(R.id.rv_order_items);

        tv_top_title = findViewById(R.id.tv_top_title);
        tv_top_title.setText("Add Items");
        item = new Item();
        sellingTypes = new ArrayList<>();
        orderItemArrayList = new ArrayList<>();

        if (orderItemArrayList.size() == 0) {
            ll_header.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.GONE);
        }
//        orderItemArrayList.add(new OrderItem("test 1","batch1",new SellingType("selname",1),10));
        adapter = new OrderItemListAdapter(AddItemActivity.this, orderItemArrayList);
        rv_order_items.setLayoutManager(new LinearLayoutManager(this));
        rv_order_items.setAdapter(adapter);

//        SharedPreferences preferences = getSharedPreferences("Order_items", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("ORDER_DATA", "[]");
//        editor.apply();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
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
                startActivity(intent);
                break;

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

//                    Log.e("response>>>", String.valueOf(response.body().size()));
//                    Log.e("request>>>", String.valueOf(call.request().url()));
                    progressDialog.dismiss();

                    item = response.body().get(0);
                    isFound = true;
//                    Log.e("name", item.getName());
//                    Log.e("barcode", item.getBarcode());
//                    Log.e("isItemFound", (String.valueOf(isFound)));

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
                        itemBatches.add(response.body().get(i));
                        Log.e("add>>", response.body().get(i).toString());
                    }
                    itemSellingTypes = new ArrayList<>();
                    itemSellingTypes.add(new SellingType("Selling type", 0));
                    PopupClass popupClass = new PopupClass();
                    if (item.getItemSellingTypes() != null) {
                        for (ItemSellingType iSt : item.getItemSellingTypes()) {
                            itemSelTypes.add(iSt);
                            for (SellingType st : sellingTypes) {
                                if (iSt.getId() == st.getId()) {
                                    itemSellingTypes.add(st);
                                }
                            }

                        }
                    }
                    popupClass.showPopupWindow(v, item, itemBatchnames, itemSellingTypes,itemBatches,itemSelTypes);

                } else {
                    Toast.makeText(AddItemActivity.this, "Cannot find requested item-batch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemBatch>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddItemActivity.this, "Connection Error ", Toast.LENGTH_SHORT).show();

            }
        });
        Log.e("batchnames size", String.valueOf(itemBatchnames.size()));
//        getSellingTypes(v);
    }

    private void getSellingTypes() {
        final ProgressDialog progressDialog = new ProgressDialog(AddItemActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Fetching Selling types...."); // set message
        progressDialog.show();
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
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<SellingType>> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
                Log.e("request>>>>>", call.request().url().toString());
                Toast.makeText(AddItemActivity.this, "Server error ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void showAndUpdateItems(View v) {
        SharedPreferences preferences = v.getContext().getSharedPreferences("Order_items", MODE_PRIVATE);
        String test = preferences.getString("ORDER_DATA", "");

        try {
            JSONArray jsonArray = new JSONArray(test);
            rv_order_items.removeAllViews();
            adapter.getOrderItemArrayList().clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.e("jsonArray.length()>>>>>>>>>>>", String.valueOf(jsonArray.length()));
                Log.e("i>>>>>>>>>>>>>>>>>>>>>>>>>>>>", String.valueOf(i));
                JSONObject object = jsonArray.getJSONObject(i);
//
                OrderItem item = new OrderItem();
                item.setName(object.getString("name"));
                item.setBatch_no(object.getString("batch_no"));
                item.setQuantity(object.getInt("quantity"));

                JSONObject selJob = object.getJSONObject("sellingType");
                SellingType sellingType = new SellingType();
                sellingType.setId(selJob.getInt("id"));
                sellingType.setName(selJob.getString("name"));

                item.setSellingType(sellingType);


                adapter.getOrderItemArrayList().add(item);

                adapter.notifyDataSetChanged();

            }

//
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ll_header.setVisibility(View.VISIBLE);
        btn_confirm.setVisibility(View.VISIBLE);

    }

    @SuppressLint("ResourceAsColor")
    private void showCustomDialog() {
//        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(AddItemActivity.this).inflate(R.layout.layout_confirm_dialog, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_confirm_message = dialogView.findViewById(R.id.tv_confirm_message);
        tv_confirm_message.setText("Are you Sure to Submit this order ?");
        AppCompatButton btn_confirm_dialog = dialogView.findViewById(R.id.btn_confirm_dialog);
        btn_confirm_dialog.setBackgroundResource(R.drawable.btn_next_background);

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

        btn_confirm_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);

    }

    private void goToHome() {
        Intent intent = new Intent(new Intent(AddItemActivity.this, MainActivity.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
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
                    if (!orderItemArrayList.isEmpty()) {
                        ll_header.setVisibility(View.VISIBLE);
                        btn_confirm.setVisibility(View.VISIBLE);
                    }
                    adapter = new OrderItemListAdapter(AddItemActivity.this, orderItemArrayList);
                    rv_order_items.setLayoutManager(new LinearLayoutManager(AddItemActivity.this));
                    rv_order_items.setAdapter(adapter);
                } else {
                    Toast.makeText(AddItemActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddItemActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
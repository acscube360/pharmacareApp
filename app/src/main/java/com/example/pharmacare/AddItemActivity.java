package com.example.pharmacare;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacare.model.Item;
import com.example.pharmacare.ui.PopupClass;
import com.example.pharmacare.utility.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout layout;
    ImageView iv_open_camera, iv_search;
    String item_code = "";
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;
    TextView tv_top_title;
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


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

        tv_top_title = findViewById(R.id.tv_top_title);
        et_search = findViewById(R.id.et_search);

        tv_top_title.setText("Add Items");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                getItemByNameOrId(et_search.getText().toString());
//                PopupClass popupClass = new PopupClass();
//                popupClass.showPopupWindow(v, item_code);
//                layout.setBackgroundColor(Color.parseColor("#606060"));
                break;
            case R.id.iv_open_camera:
                Intent intent = new Intent(v.getContext(), ScanBarcodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("item_code", "");
                startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
//        popupWithItem();
        super.onResume();

    }

    private void popupWithItem() {
//        if (!item_code.isEmpty()) {
//            handler.postDelayed(runnable = new Runnable() {
//                public void run() {
//                    handler.postDelayed(runnable, delay);
//                    PopupClass popupClass = new PopupClass();
//                    popupClass.showPopupWindow(getWindow().getDecorView().getRootView(), item_code);
//                }
//            }, delay);
//
////            Toast.makeText(this, item_code, Toast.LENGTH_SHORT).show();
//        }
    }


    private void getItemByNameOrId(String name) {
        final ProgressDialog progressDialog = new ProgressDialog(AddItemActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show();
        RetrofitClient.getInstance().getMyApi().getItemByNameOrId(name).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){

                    PopupClass popupClass = new PopupClass();
                    popupClass.showPopupWindow(getCurrentFocus(), response.body().get(0));

                }else{
                    Toast.makeText(AddItemActivity.this, response.raw().toString(), Toast.LENGTH_SHORT).show();
                }
//                Log.d("response>>>", String.valueOf(response.body().size()));

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();

            }
        });
    }
}
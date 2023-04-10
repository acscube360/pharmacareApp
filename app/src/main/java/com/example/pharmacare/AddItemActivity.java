package com.example.pharmacare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pharmacare.ui.PopupClass;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout layout;
    ImageView iv_open_camera;
    String item_code = "";
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        findViewById(R.id.iv_search).setOnClickListener(this);
        item_code = getIntent().getStringExtra("item_code");
        layout = findViewById(R.id.constraint);
        iv_open_camera = findViewById(R.id.iv_open_camera);
        iv_open_camera.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                PopupClass popupClass = new PopupClass();
                popupClass.showPopupWindow(v,item_code);
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
        popupWithItem();
        super.onResume();

    }

    private void popupWithItem(){
        if (!item_code.isEmpty()) {
            handler.postDelayed(runnable = new Runnable() {
                public void run() {
                    handler.postDelayed(runnable, delay);
                    PopupClass popupClass = new PopupClass();
                    popupClass.showPopupWindow(getWindow().getDecorView().getRootView(),item_code);
                }
            }, delay);

//            Toast.makeText(this, item_code, Toast.LENGTH_SHORT).show();
        }
    }
}
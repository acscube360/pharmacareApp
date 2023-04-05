package com.example.pharmacare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.pharmacare.ui.PopupClass;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        findViewById(R.id.iv_search).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_search:
                PopupClass popupClass=new PopupClass();
                popupClass.showPopupWindow(v);
        }
    }
}
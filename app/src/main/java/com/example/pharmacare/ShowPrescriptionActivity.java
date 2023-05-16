package com.example.pharmacare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ShowPrescriptionActivity extends AppCompatActivity {

    private ImageView iv_presc;
    private String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prescription);
        url=getIntent().getStringExtra("url");
        initView(url);
    }

    private void initView(String url) {
        iv_presc=findViewById(R.id.iv_presc);
        Glide.with(this).load(url).into(iv_presc);
    }
}
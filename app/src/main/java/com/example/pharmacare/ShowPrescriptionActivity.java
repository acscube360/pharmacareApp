package com.example.pharmacare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pharmacare.adapter.ImageAdapter;

import java.util.ArrayList;

public class ShowPrescriptionActivity extends AppCompatActivity {

    private ImageView iv_presc;
    private String url = "";
    private ArrayList<String> prescriptionList;
    ViewPager vp_prescriptions;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    private int iItem = 0;
    private TextView tv_top_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prescription);
        url = getIntent().getStringExtra("url");
        prescriptionList = new ArrayList<>();
        prescriptionList.add("https://firebasestorage.googleapis.com/v0/b/testpharamacare.appspot.com/o/images%2F81e39bd9-287a-4398-a187-ff857347b351?alt=media&token=33aab02c-a185-4907-94d4-15b6dfa2e576&_gl=1*mozowu*_ga*NTUxODU0MDIuMTY3MzA5MDA2NA..*_ga_CW55HF8NVT*MTY4NTcxOTA5Ni42LjEuMTY4NTcxOTc5NC4wLjAuMA..");
        prescriptionList.add("https://firebasestorage.googleapis.com/v0/b/testpharamacare.appspot.com/o/images%2F4917d9f7-e647-43c0-8a84-76428a8e6591?alt=media&token=5c7a34ad-3d54-4cc2-ada7-8447e1ee05df&_gl=1*irdx7r*_ga*NTUxODU0MDIuMTY3MzA5MDA2NA..*_ga_CW55HF8NVT*MTY4NTcxOTA5Ni42LjEuMTY4NTcxOTgxNi4wLjAuMA..");
        prescriptionList.add("https://firebasestorage.googleapis.com/v0/b/testpharamacare.appspot.com/o/images%2F58c0f9fd-8ecc-4005-9daf-fe78832209b0?alt=media&token=94876ef7-bd1f-4594-84f2-4c45f8350bb2&_gl=1*ytebve*_ga*NTUxODU0MDIuMTY3MzA5MDA2NA..*_ga_CW55HF8NVT*MTY4NTcxOTA5Ni42LjEuMTY4NTcxOTg0My4wLjAuMA..");
        prescriptionList.add("https://firebasestorage.googleapis.com/v0/b/testpharamacare.appspot.com/o/images%2F94a76950-513e-44cd-9450-6e2c19d469c7?alt=media&token=72c05df4-dfb1-4fa5-b325-6ddbadb183af&_gl=1*tlphns*_ga*NTUxODU0MDIuMTY3MzA5MDA2NA..*_ga_CW55HF8NVT*MTY4NTcxOTA5Ni42LjEuMTY4NTcxOTg2NS4wLjAuMA..");


//        url=prescriptionList.get(3);
        initView(prescriptionList);
    }

    private void initView(ArrayList<String> prescriptionList) {
//        iv_presc=findViewById(R.id.iv_presc);
        vp_prescriptions = findViewById(R.id.vp_prescriptions);
        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), prescriptionList);
        vp_prescriptions.setAdapter(imageAdapter);
        tv_top_title = findViewById(R.id.tv_top_title);
        tv_top_title.setText("Prescription "+(iItem+1));

        vp_prescriptions.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent event) {
                                                    switch (event.getAction()) {
                                                        case MotionEvent.ACTION_DOWN:
                                                            x1 = event.getX();
                                                            break;
                                                        case MotionEvent.ACTION_UP:
                                                            x2 = event.getX();
                                                            float deltaX = x2 - x1;

                                                            if (Math.abs(deltaX) > MIN_DISTANCE) {
                                                                // Left to Right swipe action
                                                                if (x2 > x1) {
                                                                    iItem--;
                                                                    Log.e("iItem",String.valueOf(iItem));
                                                                    if (iItem>=0){
                                                                        vp_prescriptions.setCurrentItem(iItem);
                                                                        tv_top_title.setText("Prescription "+(iItem+1));
                                                                    }else{
                                                                        iItem++;
                                                                        onBackPressed();
//                                                                        Log.e("iItem",String.valueOf(iItem));
//                                                                        Toast.makeText(ShowPrescriptionActivity.this, "you rach start", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }

                                                                // Right to left swipe action
                                                                else {
//                                                                    Toast.makeText(v.getContext(), "Right to left swipe [Next]", Toast.LENGTH_SHORT).show();
                                                                    iItem++;
                                                                    Log.e("iItem",String.valueOf(iItem));
                                                                    if (iItem<prescriptionList.size()){
                                                                        vp_prescriptions.setCurrentItem(iItem);
                                                                        tv_top_title.setText("Prescription "+(iItem+1));
                                                                    }else{
                                                                        iItem--;
//                                                                        Log.e("iItem",String.valueOf(iItem));
                                                                        Toast.makeText(ShowPrescriptionActivity.this, "you reach the end of images", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                            } else {
                                                                // consider as something else - a screen tap for example
                                                            }
                                                            break;
                                                    }
                                                    return true;
                                                }
                                            }
        );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);

    }

}
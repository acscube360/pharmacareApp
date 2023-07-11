package com.example.pharmacare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pharmacare.model.Item;
import com.example.pharmacare.ui.PopupClass;
import com.example.pharmacare.utility.RetrofitClient;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanBarcodeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ScanBarcodeActivity";
    Button btn_scan;
    DecoratedBarcodeView barcodeView;
    boolean isBarcodeDetected = false;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    TextView tv_scan, tv_top_title;
    private String barcodeData;
    private View rootView;
    private boolean isFoundItem = false;
    private Item resultItem;
    private boolean fromSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        fromSearch = getIntent().getBooleanExtra("fromSearch", false);
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);
        surfaceView = findViewById(R.id.surface_view);
        rootView = findViewById(R.id.barcode_layout);
        tv_top_title = findViewById(R.id.tv_top_title);
        tv_top_title.setText("Scan Bar Code");
        resultItem = new Item();
        initialiseDetectorsAndSources();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (!isBarcodeDetected && !isFoundItem) {

                    surfaceView.setVisibility(View.VISIBLE);
                } else {
                    if (fromSearch) {
                        onBackPressed();
                    } else {
                        Intent intent = new Intent(ScanBarcodeActivity.this, AddItemActivity.class);
                        intent.putExtra("item_code", btn_scan.getText().toString());  // pass your values and retrieve them in the other Activity using AnyKeyName
                        startActivity(intent);
                    }
                }
//                startQRScanner();

                break;
        }
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanBarcodeActivity.this, new
                                String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    isBarcodeDetected = true;
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            cameraSource.stop();

                            //  tv_scan.setText(barcodes.valueAt(0).displayValue);
                            getItemByNameOrId(barcodes.valueAt(0).displayValue);
                            //  btn_scan.setText(barcodes.valueAt(0).displayValue);
                        }
                    });


                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraSource.stop();
//        initialiseDetectorsAndSources();


    }

    private void getItemByNameOrId(String name) {
        SharedPreferences sp = getSharedPreferences("ItemBarcode", 0);
        SharedPreferences.Editor editor = sp.edit();

        final ProgressDialog progressDialog = new ProgressDialog(ScanBarcodeActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show();
        RetrofitClient.getInstance().getMyApi().getItemByNameOrId(name).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    editor.putString("barcode", response.body().get(0).getBarcode());
                    editor.putString("itemName", response.body().get(0).getName());
                    editor.commit();
                    btn_scan.setText(response.body().get(0).getName());
                    isFoundItem = true;

                } else {
                    isFoundItem = false;
                    Toast.makeText(ScanBarcodeActivity.this, "", Toast.LENGTH_SHORT).show();
                }
//                Log.d("response>>>", String.valueOf(response.body().size()));

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                t.printStackTrace();
                isFoundItem = false;
                progressDialog.dismiss();

            }
        });
    }

    private void setResponsToItemObject(Response<List<Item>> response) {

        resultItem.setId(response.body().get(0).getId());
        resultItem.setName(response.body().get(0).getName());
        resultItem.setMeasurement(response.body().get(0).getMeasurement());
        resultItem.setImageUrl(response.body().get(0).getImageUrl());
        resultItem.setBarcode(response.body().get(0).getBarcode());

    }
}

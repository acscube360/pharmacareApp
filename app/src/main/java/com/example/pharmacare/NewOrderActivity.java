package com.example.pharmacare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pharmacare.adapter.ShowAddedPrescriptionsAdapter;

import java.util.ArrayList;

public class NewOrderActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "NewOrderActivity";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        builder = new AlertDialog.Builder(this);

        findViewById(R.id.iv_open_camera).setOnClickListener(this);


    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(NewOrderActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(NewOrderActivity.this, new String[]{permission}, requestCode);
        } else {
            //Toast.makeText(NewOrderActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "checkPermission: Permission already granted");
            openGallery();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_camera:
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: permisson granted");
               // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
               showAlertDialog(R.string.alert_denied_title,R.string.camera_denied);

            }
        }
        //else if (){}
    }
    private void showAlertDialog(int title,int message){

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Allow permission", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
    }


}

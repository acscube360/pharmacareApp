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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pharmacare.adapter.ShowAddedPrescriptionsAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewOrderActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "NewOrderActivity";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    int CODE = 20;
    AlertDialog.Builder builder;
    ShowAddedPrescriptionsAdapter prescriptionsAdapter;
    ArrayList<Uri> uriArrayList;
    RecyclerView recView_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        builder = new AlertDialog.Builder(this);
        uriArrayList = new ArrayList<>();
        findViewById(R.id.iv_open_camera).setOnClickListener(this);
        prescriptionsAdapter = new ShowAddedPrescriptionsAdapter(uriArrayList);
        recView_selected = findViewById(R.id.recView_selected);
        recView_selected.setLayoutManager(new GridLayoutManager(NewOrderActivity.this, 3));
        recView_selected.setAdapter(prescriptionsAdapter);
        prescriptionsAdapter.notifyDataSetChanged();


    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(NewOrderActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(NewOrderActivity.this, new String[]{permission}, requestCode);
        } else {
            //Toast.makeText(NewOrderActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "checkPermission: Permission already granted");
            //openGallery();
            openCamera();
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
private void openCamera(){
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, CODE);
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    uriArrayList.add(data.getClipData().getItemAt(i).getUri());
                }
                prescriptionsAdapter.notifyDataSetChanged();
            } else if (data.getData() != null) {
                String imgurl = data.getData().getPath();
                uriArrayList.add(Uri.parse(imgurl));
            }
        }
        if (requestCode == CODE) {
            if (resultCode == RESULT_OK) {
//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//                Intent intent = new Intent();
//                intent.putExtra("check_Image",data);
//                //img.setImageBitmap(bitmap);
                if (data.getClipData() != null) {
                    int x = data.getClipData().getItemCount();
                    for (int i = 0; i < x; i++) {
                        uriArrayList.add(data.getClipData().getItemAt(i).getUri());
                    }
                    prescriptionsAdapter.notifyDataSetChanged();
                } else if (data.getData() != null) {
//                    String imgurl = data.getData().getPath();
//                    path =
                    uriArrayList.add(Uri.parse(getIntent().getExtras().getString("path")));
                }
            }
        }
    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: permisson granted");
                // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                showAlertDialog(R.string.alert_denied_title, R.string.camera_denied);

            }
        }
        //else if (){}
    }

    private void showAlertDialog(int title, int message) {

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

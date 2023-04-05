package com.example.pharmacare;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.example.pharmacare.adapter.ShowAddedPrescriptionsAdapter;

import java.util.ArrayList;

public class AddNewOrderActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_open_camera, iv_open_gallery;
    Uri image_uri;
    ShowAddedPrescriptionsAdapter prescriptionsAdapter;
    ArrayList<Uri> uriArrayList;
    RecyclerView recView_selected;
    AppCompatButton btn_back, btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        uriArrayList = new ArrayList<>();
        findViewById(R.id.iv_open_camera).setOnClickListener(this);
        findViewById(R.id.iv_open_gallery).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);

        recView_selected = findViewById(R.id.recView_selected);
        prescriptionsAdapter = new ShowAddedPrescriptionsAdapter(uriArrayList);
        recView_selected.setLayoutManager(new GridLayoutManager(AddNewOrderActivity.this, 3));
        recView_selected.setAdapter(prescriptionsAdapter);
        prescriptionsAdapter.notifyDataSetChanged();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permission = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 112);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_camera:
                openCamera();
                break;
            case R.id.iv_open_gallery:
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                galleryActivityResultLauncher.launch(galleryIntent);
                openGallery();
                break;
            case R.id.btn_next:
                Intent intent=new Intent(new Intent(v.getContext(),AddItemActivity.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

                break;

            case R.id.btn_back:
                onBackPressed();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        uriArrayList.add(image_uri);
                        prescriptionsAdapter.notifyDataSetChanged();
//                        Bitmap inputImage = uriToBitmap(image_uri);
//                        Bitmap rotated = rotateBitmap(inputImage);
//                        imageView.setImageBitmap(rotated);

                    }
                }
            });
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
        galleryActivityResultLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData().getClipData() != null) {
                            int x = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < x; i++) {
                                uriArrayList.add(result.getData().getClipData().getItemAt(i).getUri());
                            }
                            prescriptionsAdapter.notifyDataSetChanged();
                        } else if (result.getData() != null) {
                            String imgurl = result.getData().getData().getPath();
                            uriArrayList.add(Uri.parse(imgurl));
                        }
                        Uri image_uri = result.getData().getData();
                        uriArrayList.add(image_uri);
                        prescriptionsAdapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
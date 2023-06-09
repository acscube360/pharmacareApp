package com.example.pharmacare;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pharmacare.adapter.ShowAddedPrescriptionsAdapter;
import com.example.pharmacare.utility.CheckNetwork;
import com.example.pharmacare.utility.IntentUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class AddNewOrderActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "";
    ImageView iv_open_camera, iv_open_gallery;
    Uri image_uri;
    ShowAddedPrescriptionsAdapter prescriptionsAdapter;
    ArrayList<Uri> uriArrayList;
    RecyclerView recView_selected;
    AppCompatButton btn_back, btn_next;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private int cameraPermissionRequestCount = 0;
    private int storagePermissionRequestCount = 0;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        uriArrayList = new ArrayList<>();
        findViewById(R.id.iv_open_camera).setOnClickListener(this);
        findViewById(R.id.iv_open_gallery).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.iv_search_bottom).setOnClickListener(this);
//
        SharedPreferences preferences = getSharedPreferences("Order_items", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ORDER_DATA", "[]");
        editor.apply();

        recView_selected = findViewById(R.id.recView_selected);
        prescriptionsAdapter = new ShowAddedPrescriptionsAdapter(uriArrayList);
        recView_selected.setLayoutManager(new GridLayoutManager(AddNewOrderActivity.this, 3));
        recView_selected.setAdapter(prescriptionsAdapter);
        prescriptionsAdapter.notifyDataSetChanged();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_camera:
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                break;
            case R.id.iv_open_gallery:
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

                break;
            case R.id.iv_search_bottom:
                IntentUtils.popupSearch(v);

                break;
            case R.id.btn_next:
//                startActivity(new Intent(this,ShowPrescriptionActivity.class));
                if (CheckNetwork.isInternetAvailable(v.getContext())) {
                    uploadImage();
                } else {
                    Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }


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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
        galleryActivityResultLauncher.launch(intent);
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
//                            String imgurl = result.getData().getData().getPath();
//                            uriArrayList.add(Uri.parse(imgurl));
//                            prescriptionsAdapter.notifyDataSetChanged();
                            Uri image_uri = result.getData().getData();
                            uriArrayList.add(image_uri);
                            prescriptionsAdapter.notifyDataSetChanged();
                        }
//                        Uri image_uri = result.getData().getData();
//                        uriArrayList.add(image_uri);
//                        prescriptionsAdapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(AddNewOrderActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            switch (requestCode) {
                case CAMERA_PERMISSION_CODE:
                    cameraPermissionRequestCount++;
                    break;
                case STORAGE_PERMISSION_CODE:
                    storagePermissionRequestCount++;
                    break;
            }

            // Requesting the permission
            ActivityCompat.requestPermissions(AddNewOrderActivity.this, new String[]{permission}, requestCode);
        } else {
            switch (requestCode) {
                case CAMERA_PERMISSION_CODE:
                    openCamera();
                    break;
                case STORAGE_PERMISSION_CODE:
                    openGallery();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(AddNewOrderActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
                openCamera();
            } else {
                showAlertDialog(cameraPermissionRequestCount, CAMERA_PERMISSION_CODE);
                //  checkPermission(Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE);
                //Toast.makeText(AddNewOrderActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                showAlertDialog(storagePermissionRequestCount, STORAGE_PERMISSION_CODE);
            }
        }
    }

    public void showAlertDialog(int count, int permissionCode) {
        String msg = "This app needs you to allow camera/Read storage permission in order to function.Will you allow it";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        if (count > 2) {
            msg = "Please allow permissions in your phone settings";
            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                        }
                    });

        } else {

            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            switch (permissionCode) {
                                case CAMERA_PERMISSION_CODE:
                                    checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                                    break;
                                case STORAGE_PERMISSION_CODE:
                                    checkPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                            }

                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        alertDialogBuilder.setMessage(msg);


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void uploadImage() {
        Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("item_code", "");
//        intent.putExtra("url", uri.toString());
        startActivity(intent);
//
//        if (uriArrayList.size() != 0) {
//
//            // Code for showing progressDialog while uploading
//            ProgressDialog progressDialog
//                    = new ProgressDialog(this);
//            progressDialog.getWindow().setGravity(Gravity.CENTER);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//
//            // Defining the child of storageReference
//            StorageReference ref
//                    = storageReference
//                    .child(
//                            "images/"
//                                    + UUID.randomUUID().toString());
//
//            // adding listeners on upload
//            // or failure of image
//            for (Uri uri : uriArrayList) {
//                ref.putFile(uri)
////                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
////                            @Override
////                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
////
////                            }
////                        })
//                        .addOnSuccessListener(
//                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                                    @Override
//                                    public void onSuccess(
//                                            UploadTask.TaskSnapshot taskSnapshot) {
//
//                                        // Image uploaded successfully
//                                        // Dismiss dialog
//
//                                        progressDialog.dismiss();
//                                        Toast
//                                                .makeText(AddNewOrderActivity.this,
//                                                        "Image Uploaded!!",
//                                                        Toast.LENGTH_SHORT)
//                                                .show();
//                                        url = taskSnapshot.getStorage().getDownloadUrl().toString();
//                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                Log.e("uri", uri.toString());
//                                                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                                intent.putExtra("item_code", "");
//                                                intent.putExtra("url", uri.toString());
//                                                startActivity(intent);
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        });
//                                    }
//                                })
//
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                e.printStackTrace();
//                                // Error, Image not uploaded
//                                progressDialog.dismiss();
//                                Toast
//                                        .makeText(AddNewOrderActivity.this,
//                                                "Failed " + e.getMessage(),
//                                                Toast.LENGTH_SHORT)
//                                        .show();
//                            }
//                        })
//
//                        .addOnProgressListener(
//                                new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                                    // Progress Listener for loading
//                                    // percentage on the dialog box
//                                    @Override
//                                    public void onProgress(
//                                            UploadTask.TaskSnapshot taskSnapshot) {
//                                        double progress
//                                                = (100.0
//                                                * taskSnapshot.getBytesTransferred()
//                                                / taskSnapshot.getTotalByteCount());
//                                        progressDialog.setMessage(
//                                                "Uploading "
//                                                        + (int) progress + "%");
//                                    }
//                                });
//
//            }
//
////            Intent intent = new Intent(this, ShowPrescriptionActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//////            intent.putExtra("item_code", "");
////            intent.putExtra("url", "");
////            startActivity(intent);
//
//
//        } else {
//            Toast.makeText(this, "Please Select Image to upload", Toast.LENGTH_SHORT).show();
//        }
    }
}
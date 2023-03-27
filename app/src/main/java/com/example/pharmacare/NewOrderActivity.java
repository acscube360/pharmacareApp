package com.example.pharmacare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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

    ImageView iv_open_camera;
    RecyclerView recView_selected;
    ShowAddedPrescriptionsAdapter prescriptionsAdapter;
    ArrayList<Uri> uriArrayList;
    String permissions[] = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String TAG = "NewOrderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        uriArrayList = new ArrayList<>();
        prescriptionsAdapter = new ShowAddedPrescriptionsAdapter(uriArrayList);
        recView_selected = findViewById(R.id.recView_selected);
        recView_selected.setLayoutManager(new GridLayoutManager(NewOrderActivity.this, 3));
        recView_selected.setAdapter(prescriptionsAdapter);
        prescriptionsAdapter.notifyDataSetChanged();

        findViewById(R.id.iv_open_camera).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_camera:
                if ((ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(permissions, 123);
                    }
                }else{
                    openGallery();
                   // Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

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
        if (requestCode == 123 && resultCode == RESULT_OK) {
            openGallery();
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

    }
}
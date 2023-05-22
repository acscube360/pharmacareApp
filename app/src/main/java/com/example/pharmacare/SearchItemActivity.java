package com.example.pharmacare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.pharmacare.model.Item;
import com.example.pharmacare.utility.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchItemActivity extends AppCompatActivity {

    private ArrayList<Item> itemArrayList;
    private ArrayList<String> itemNamesArraylist;
    private AutoCompleteTextView auto_tv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        auto_tv_search=findViewById(R.id.auto_tv_search);
        loadItems();

    }

    private void loadItems() {
        final ProgressDialog progressDialog = new ProgressDialog(SearchItemActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Fetching data...."); // set message
        progressDialog.show();
        itemArrayList = new ArrayList<>();
        itemNamesArraylist = new ArrayList<>();
        RetrofitClient.getInstance().getMyApi().getAllItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().size(); i++) {
                        itemArrayList.add(response.body().get(i));
                        itemNamesArraylist.add(response.body().get(i).getName());
                    }
                    Log.e("itemListSize>>>>>>>", String.valueOf(itemArrayList.size()));
                    Log.e("itemNameSize>>>>>>>", String.valueOf(itemNamesArraylist.size()));
                    ArrayAdapter adapter = new ArrayAdapter
                            (getApplicationContext(), android.R.layout.select_dialog_item, itemNamesArraylist);
                    auto_tv_search.setThreshold(1);
                    auto_tv_search.setAdapter(adapter);

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(SearchItemActivity.this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
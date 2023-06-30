package com.example.pharmacare.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.pharmacare.AddItemActivity;
import com.example.pharmacare.R;
import com.example.pharmacare.ScanBarcodeActivity;
import com.example.pharmacare.SearchItemActivity;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.model.ItemCategory;
import com.example.pharmacare.model.ItemSubCategory;
import com.example.pharmacare.utility.CheckNetwork;
import com.example.pharmacare.utility.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopupSearchView {
    private ImageView iv_close, iv_open_barcode;
    private Button btn_cancel, btn_search;
    private EditText et_barcode, et_item_id, et_item_name;
    private ArrayList<ItemBatch> itemBatches;
    private ArrayList<ItemCategory> itemCategories;
    private ArrayList<ItemSubCategory> itemSubCategories;
    private Spinner category_spinner, sub_category_spinner;

    ArrayList<String> catNames = new ArrayList<>();
    ArrayList<String> subCatNames = new ArrayList<>();

    public void showPopupSearchView(View view) {
        getAllCategories(view);
        getAllSubCategories(view);
        itemCategories = new ArrayList<>();
        itemSubCategories = new ArrayList<>();
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.search_item_popup_view, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.search_item_popup_view, null);
        //Specify the length and width through constants

//        int width = LinearLayout.LayoutParams.MATCH_PARENT;
//        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
//        boolean focusable = true;

        //Create a window with our parameters
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen

//        popupWindow.setOutsideTouchable(false);
//        popupWindow.setAnimationStyle(R.style.popup_window_animation_phone);
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        iv_close = dialogView.findViewById(R.id.iv_close);
        btn_search = dialogView.findViewById(R.id.btn_search);
        et_barcode = dialogView.findViewById(R.id.et_barcode);
        et_item_id = dialogView.findViewById(R.id.et_item_id);
        et_item_name = dialogView.findViewById(R.id.et_item_name);
        iv_open_barcode = dialogView.findViewById(R.id.iv_open_barcode);

        iv_open_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.getContext().startActivity(new Intent(v.getContext(), ScanBarcodeActivity.class));

            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                popupWindow.dismiss();
                alertDialog.dismiss();
            }
        });
        btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popupWindow.dismiss();
                alertDialog.dismiss();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllEmpty()) {
                    Toast.makeText(dialogView.getContext(), "Required Barcode /Item name / Item Id", Toast.LENGTH_SHORT).show();
                } else {
                    if (CheckNetwork.isInternetAvailable(v.getContext())) {
                        final ProgressDialog progressDialog = new ProgressDialog(v.getContext());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setCancelable(false); // set cancelable to false
                                progressDialog.setMessage("Searching...."); // set message
                                progressDialog.show();
                            }
                        });

                        itemBatches = new ArrayList<>();
                        RetrofitClient.getInstance().getMyApi().getItemBatches(et_item_name.getText().toString()).enqueue(new Callback<List<ItemBatch>>() {
                            @Override
                            public void onResponse(Call<List<ItemBatch>> call, Response<List<ItemBatch>> response) {
                                if (response.isSuccessful()) {

                                    progressDialog.dismiss();
                                    Log.e("response>>", response.body().toString());
                                    for (int i = 0; i < response.body().size(); i++) {
                                        itemBatches.add(response.body().get(i));
                                        Log.e("add>>", response.body().get(i).getName());
                                    }
                                    Intent intent = new Intent(v.getContext(), SearchItemActivity.class);
                                    intent.putExtra("itemBatches", itemBatches);
                                    v.getContext().startActivity(intent);

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(view.getContext(), "Cannot find requested item", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<List<ItemBatch>> call, Throwable t) {
                                t.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(view.getContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(view.getContext(), "Please Check your internet connection!", Toast.LENGTH_SHORT).show();

                    }

//                    v.getContext().startActivity(new Intent(v.getContext(), SearchItemActivity.class));

                }
            }
        });

        sub_category_spinner = dialogView.findViewById(R.id.sub_category_spinner);
        category_spinner = dialogView.findViewById(R.id.category_spinner);
        catNames = new ArrayList<>();
        catNames.add("Select Category");
        subCatNames.add("Select Sub Category");
        ArrayAdapter catAdapter = new ArrayAdapter(view.getContext(), R.layout.spinner_item, catNames);
//        catAdapter.setDropDownViewResource(R.layout.spinner_item);
        category_spinner.setAdapter(catAdapter);

        ArrayAdapter subCatAdapter = new ArrayAdapter(view.getContext(), R.layout.spinner_item, subCatNames);
//        subCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub_category_spinner.setAdapter(subCatAdapter);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    subCatNames=new ArrayList<>();
                    subCatNames=setItemSubCategoryNames(itemSubCategories, itemCategories.get(position - 1).getId());
                    subCatAdapter.clear();
                    subCatAdapter.addAll(subCatNames);
                    Log.e("selected", itemCategories.get(position - 1).getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        sub_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("selected", String.valueOf(position));
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        alertDialog.show();

    }

    private boolean isAllEmpty() {
        if (et_barcode.getText().toString().isEmpty() && et_item_id.getText().toString().isEmpty() && et_item_name.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }


    private void getAllCategories(View v) {
        catNames = new ArrayList<>();

        if (CheckNetwork.isInternetAvailable(v.getContext())) {
            final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
            progressDialog.setCancelable(false); // set cancelable to false
            progressDialog.setMessage("Fetching data...."); // set message
            progressDialog.show();
            itemBatches = new ArrayList<>();
            RetrofitClient.getInstance().getMyApi().getAllCategories().enqueue(new Callback<List<ItemCategory>>() {
                @Override
                public void onResponse(Call<List<ItemCategory>> call, Response<List<ItemCategory>> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().size(); i++) {
                            itemCategories.add(response.body().get(i));
                            catNames.add(response.body().get(i).getName());
                            Log.e("add>>", response.body().get(i).getName());
                        }
//                        setItemCategoryNames(itemCategories);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(v.getContext(), "Server Error!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<List<ItemCategory>> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    Toast.makeText(v.getContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(v.getContext(), "Please Check your internet connection!", Toast.LENGTH_SHORT).show();

        }
    }

    private void getAllSubCategories(View v) {
        if (CheckNetwork.isInternetAvailable(v.getContext())) {
            final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
            progressDialog.setCancelable(false); // set cancelable to false
            progressDialog.setMessage("Fetching data...."); // set message
            progressDialog.show();
            itemBatches = new ArrayList<>();
            RetrofitClient.getInstance().getMyApi().getAllSubCategories().enqueue(new Callback<List<ItemSubCategory>>() {
                @Override
                public void onResponse(Call<List<ItemSubCategory>> call, Response<List<ItemSubCategory>> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().size(); i++) {
                            itemSubCategories.add(response.body().get(i));
                            Log.e("addSubCat>>", response.body().get(i).getName());
                            subCatNames.add(response.body().get(i).getName());
                        }
//                        setItemSubCategoryNames(itemSubCategories);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(v.getContext(), "Server Error!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<List<ItemSubCategory>> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    Toast.makeText(v.getContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(v.getContext(), "Please Check your internet connection!", Toast.LENGTH_SHORT).show();

        }
    }

    private void setItemCategoryNames(ArrayList<ItemCategory> list) {
//        catNames.add("Select Category");
//        catNames.add("Select Category1");
//        catNames.add("Select Category2");
//        catNames.add("Select Category3");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
//        catNames.add("Select Category");
        catNames.add("Select Category");
        for (ItemCategory itemCategory : list) {
            catNames.add(itemCategory.getName());
        }

    }

    private ArrayList<String> setItemSubCategoryNames(ArrayList<ItemSubCategory> list, int cat_id) {
        subCatNames.add("Select Sub Category");

        for (ItemSubCategory itemSubCategory : list) {
            if (itemSubCategory.getItemCategoryId() == cat_id) {
                subCatNames.add(itemSubCategory.getName());
            }
        }
        return subCatNames;
    }


}

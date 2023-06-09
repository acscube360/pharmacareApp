package com.example.pharmacare.ui;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.pharmacare.AddItemActivity;
import com.example.pharmacare.R;
import com.example.pharmacare.model.Item;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.model.ItemSellingType;
import com.example.pharmacare.model.OrderItem;
import com.example.pharmacare.model.SellingType;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopupClass {
    //PopupWindow display method
    private ArrayList<ItemBatch> itemBatchArrayList;
    private ArrayList<String> itemBatchnames = new ArrayList<>();
    ArrayList<SellingType> mSellingTypes = new ArrayList<>();
    ArrayList<ItemSellingType> mItemSellingTypes = new ArrayList<>();
    private Spinner batch_spinner, sell_type_spinner;
    AppCompatButton btn_cancel, btn_add;
    ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();
    private String itemBatch = "";
    private SellingType itemSellingType = new SellingType();
    private int quantity = 0;
    private EditText et_item_count;
    JSONArray jsonArray = new JSONArray();
    private int stock = 0;
    private int sellTypeCapacity = 1;
    private ImageView iv_close;

    public void showPopupWindow(final View view, Item item, ArrayList<String> batches, ArrayList<SellingType> sellingTypes, ArrayList<ItemBatch> itemBatches, ArrayList<ItemSellingType> itemSelTypes) {

        itemBatchnames = batches;
        mSellingTypes = sellingTypes;
        mItemSellingTypes = itemSelTypes;
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_item_popup_window, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen

        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popup_window_animation_phone);

        //Initialize the elements of our window, install the handler

        TextView test2 = popupView.findViewById(R.id.titleText);

        test2.setText(item.getName());

        ImageView iv_close = popupView.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
//  getItemBatches(item.getName());
//        new GetItemBatchAsyncTask().execute("",item.getName(),"");
        batch_spinner = popupView.findViewById(R.id.batch_spinner);
        sell_type_spinner = popupView.findViewById(R.id.sell_type_spinner);
        et_item_count = popupView.findViewById(R.id.et_item_count);
        btn_cancel = popupView.findViewById(R.id.btn_cancel);
        iv_close = popupView.findViewById(R.id.iv_close);

        ArrayAdapter aa = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, itemBatchnames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        batch_spinner.setAdapter(aa);

        ArrayAdapter selTypeAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, getSellTypeStrings(mSellingTypes));
        selTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sell_type_spinner.setAdapter(selTypeAdapter);

        batch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position==0){
//                    ((TextView) parent.getChildAt(0)).setTextColor(R.color.et_hint);
//                }
                itemBatch = itemBatchnames.get(position);
                stock = itemBatches.get(position).getStock();
                sell_type_spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sell_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position==0){
//                    ((TextView) parent.getChildAt(0)).setTextColor(R.color.et_hint);
//                }
                itemSellingType = mSellingTypes.get(position);
                sellTypeCapacity = mItemSellingTypes.get(position).getCapacity();
//                Log.e("sellType position", String.valueOf(position));
//                Log.e("itemSellingType", String.valueOf(mSellingTypes.get(position).getName()));
//                Log.e("sellTypeCapacity", String.valueOf(mItemSellingTypes.get(position).getCapacity()));

                if (position != 0) {

                    et_item_count.setHint("You can sell about " + String.valueOf(stock / sellTypeCapacity) + " " + itemSellingType.getName() + " /s");

                }else{
                    et_item_count.setHint("Quantity");
                }
                et_item_count.setHintTextColor(R.color.black);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        et_item_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int q = 0;
                try {
                    q = Integer.parseInt(et_item_count.getText().toString());
                    if (q * sellTypeCapacity > stock) {
                        Toast.makeText(view.getContext(), "exceed quantity", Toast.LENGTH_SHORT).show();
                    } else {
                        quantity = q;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_add = popupView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantity = Integer.parseInt(et_item_count.getText().toString());

                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    quantity = 0;
                }
                if (isAllSet(v)) {
//                    Toast.makeText(v.getContext(), "all data is set", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = v.getContext().getSharedPreferences("Order_items", MODE_PRIVATE);
                    String test = preferences.getString("ORDER_DATA", "");
                    OrderItem orderItem = new OrderItem(item.getName(), itemBatch, itemSellingType, quantity);
//
                    try {
                        jsonArray = new JSONArray(test);
                        jsonArray.put(new JSONObject(new Gson().toJson(orderItem)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    Log.e("jsonArray", jsonArray.toString());
                    editor.putString("ORDER_DATA", jsonArray.toString());
                    editor.apply();

                    AddItemActivity.showAndUpdateItems(v);
                    popupWindow.dismiss();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private ArrayList<String> getSellTypeStrings(ArrayList<SellingType> sellingTypes) {

        ArrayList<String> sellTypesStrings = new ArrayList<>();

        if (sellingTypes.size() > 0) {
            for (SellingType sType : sellingTypes) {
                sellTypesStrings.add(sType.getName());
            }
        }

        return sellTypesStrings;
    }

    private boolean isAllSet(View v) {
        boolean isAllSEt = false;
        if (itemBatch.isEmpty()) {
            isAllSEt = false;
            Toast.makeText(v.getContext(), "Please select item batch", Toast.LENGTH_SHORT).show();

        } else if (itemSellingType.getName().isEmpty()) {
            isAllSEt = false;
            Toast.makeText(v.getContext(), "Please select item batch", Toast.LENGTH_SHORT).show();
        } else if (quantity < 1) {
            isAllSEt = false;
            Toast.makeText(v.getContext(), "Please enter valid quantity", Toast.LENGTH_SHORT).show();

        } else {
            isAllSEt = true;
        }
        return isAllSEt;
    }
}

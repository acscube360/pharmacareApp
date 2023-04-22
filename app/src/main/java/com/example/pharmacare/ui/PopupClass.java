package com.example.pharmacare.ui;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.pharmacare.R;
import com.example.pharmacare.model.Item;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.model.OrderItem;
import com.example.pharmacare.model.SellingType;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PopupClass {
    //PopupWindow display method
    private ArrayList<ItemBatch> itemBatchArrayList;
    private ArrayList<String> itemBatchnames = new ArrayList<>();
    ArrayList<SellingType> mSellingTypes = new ArrayList<>();
    private Spinner batch_spinner, sell_type_spinner;
    AppCompatButton btn_cancel, btn_add;
    ArrayList<OrderItem> orderItemArrayList=new ArrayList<>();

    public void showPopupWindow(final View view, Item item, ArrayList<String> batches, ArrayList<SellingType> sellingTypes) {

        itemBatchnames = batches;
        mSellingTypes = sellingTypes;
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

        ArrayAdapter aa = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, itemBatchnames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        batch_spinner.setAdapter(aa);

        ArrayAdapter selTypeAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, getSellTypeStrings(mSellingTypes));
        selTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sell_type_spinner.setAdapter(selTypeAdapter);

        batch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // batch_spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sell_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_add = popupView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}

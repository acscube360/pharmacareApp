package com.example.pharmacare.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacare.R;
import com.example.pharmacare.adapter.ActiveOrderAdapter;
import com.example.pharmacare.adapter.CompletedOrderAdapter;
import com.example.pharmacare.model.Order;
import com.example.pharmacare.utility.DeletionSwipeHelper;
import com.example.pharmacare.utility.RetrofitClient;
import com.example.pharmacare.utility.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//implements DeletionSwipeHelper.OnSwipeListener

public class ActiveOrderFragment extends Fragment  {
    ArrayList<Order> activeOrderArrayList;
    RecyclerView rv_completed_order;
    View view;
    ActiveOrderAdapter adapter;

    public ActiveOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_active_order, container, false);
        activeOrderArrayList = new ArrayList<>();
        rv_completed_order = view.findViewById(R.id.rv_active_order);

        adapter = new ActiveOrderAdapter(getActivity(), getOrders());
        rv_completed_order.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // rv_completed_order.setLayoutManager(linearLayoutManager);
        rv_completed_order.setAdapter(adapter);
//        populateList(getOrders());
        enableSwipeToDeleteAndUndo();
        return view;
    }

    private void getActiveOrderList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show();

        RetrofitClient.getInstance().getMyApi().getAllOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
//                Log.e(TAG, "onResponse: "+response.body().toString());
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().size(); i++) {
//                        if (response.body().get(i).getStatus() == 3) {
                        Order order = new Order();
                        order = response.body().get(i);
                        activeOrderArrayList.add(order);
//                        }
                    }
                    populateList(activeOrderArrayList);
                } else {
                    Log.e("error", "error");
                }

                progressDialog.dismiss();
                Log.e("TAG", "completedOrderSize" + activeOrderArrayList.size());

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    private void populateList(ArrayList<Order> orders) {
        adapter = new ActiveOrderAdapter(getActivity(), orders);
        rv_completed_order.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // rv_completed_order.setLayoutManager(linearLayoutManager);
        rv_completed_order.setAdapter(adapter);
        // progressDialog.dismiss();
    }

    public void filterTransactions(String searchText) {
//        if (rv_completed_order != null && rv_completed_order.getAdapter() != null && !searchText.isEmpty()) {
//            // mAdapter.filterData(search_text);
//            try {
//                ((CompletedOrderAdapter) rv_completed_order.getAdapter()).getFilter().filter(searchText);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
    }

//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int position) {
//        // buttonShowDialog_onClick(view, activeOrderArrayList.get(position).getRemark(), position, viewHolder);
//       // showConfirmDialog();
//        showCustomDialog();
////        ((ActiveOrderAdapter.ViewHolder)viewHolder).removeItem(position);
//    }

    private ArrayList<Order> getOrders() {
        activeOrderArrayList = new ArrayList<>();
        activeOrderArrayList.add(new Order("test1", "2023/04/14", "aruna"));
        activeOrderArrayList.add(new Order("test2", "2023/04/15", "aruna2"));
        activeOrderArrayList.add(new Order("test3", "2023/04/16", "aruna4"));
        activeOrderArrayList.add(new Order("test55", "2023/04/15", "aruna6"));
        activeOrderArrayList.add(new Order("test3", "2023/04/15", "aruna0"));


        return activeOrderArrayList;
    }

    private void buttonShowDialog_onClick(View view, String orderId, int position, RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to DELETE this order" + orderId);
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((ActiveOrderAdapter.ViewHolder) viewHolder).removeItem(position);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder
                        (getContext(), R.style.Theme_Pharmacare);
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.layout_confirm_dialog, null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });

        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.CENTER);


    }
    private void showCustomDialog() {
//        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_confirm_dialog,null,false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Order item = adapter.getItem(position);

                adapter.removeItem(position);

                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_confirm_dialog,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.restoreItem(item, position);
                        rv_completed_order.scrollToPosition(position);
                        alertDialog.dismiss();
                    }
                });


                alertDialog.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rv_completed_order);
    }

}
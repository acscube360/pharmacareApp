package com.example.pharmacare.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacare.R;
import com.example.pharmacare.adapter.ActiveOrderAdapter;
import com.example.pharmacare.model.IOrderDetailsSearch;
import com.example.pharmacare.model.Order;
import com.example.pharmacare.utility.CheckNetwork;
import com.example.pharmacare.utility.RetrofitClient;
import com.example.pharmacare.utility.SwipeToDeleteCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//implements DeletionSwipeHelper.OnSwipeListener

public class ActiveOrderFragment extends Fragment implements IOrderDetailsSearch {
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

        if (CheckNetwork.isInternetAvailable(getActivity())) {
            getActiveOrderList();

        } else {
            Toast.makeText(getActivity(), "Please heck youe internet connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getActiveOrderList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show();

        RetrofitClient.getInstance().getMyApi().getActiveOrders().enqueue(new Callback<List<Order>>() {
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
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    Log.e("error", "error");
                }

                progressDialog.dismiss();
                Log.e("TAG", "completedOrderSize" + activeOrderArrayList.size());

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Error when loading Active order", Toast.LENGTH_SHORT).show();

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
        enableSwipeToDeleteAndUndo();
    }

    public void filterTransactions(String searchText) {
        if (rv_completed_order != null && rv_completed_order.getAdapter() != null) {
            // mAdapter.filterData(search_text);
            try {
                ((ActiveOrderAdapter) rv_completed_order.getAdapter()).getFilter().filter(searchText);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void showCustomDialog() {
//        ViewGroup viewGroup = findViewById(android.R.id.content);

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Order order = adapter.getOrderList().get(position);
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_confirm_dialog, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                String msg = "Are you Sure to Delete this order " + adapter.getItem(position).getRemark();
                TextView tv_confirm_message = dialogView.findViewById(R.id.tv_confirm_message);
                tv_confirm_message.setText(msg);
                adapter.removeItem(position);
                dialogView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.restoreItem(order, position);
                        rv_completed_order.scrollToPosition(position);
                        alertDialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.restoreItem(order, position);
                        rv_completed_order.scrollToPosition(position);
                        alertDialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.btn_confirm_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RetrofitClient.getInstance().getMyApi().deleteActiveOrder(order.getId()).enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                Log.e("request", String.valueOf(call.request().url()));
                                Log.e("response", response.body().toString());

                                if (!response.isSuccessful()) {
                                    adapter.restoreItem(order, position);
                                }
                                rv_completed_order.scrollToPosition(position);
                                alertDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                t.printStackTrace();
                                adapter.restoreItem(order, position);
                                rv_completed_order.scrollToPosition(position);
                                alertDialog.dismiss();
                                alertDialog.show();
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rv_completed_order);


    }

    @Override
    public void onSearch(String inputString) {
        filterTransactions(inputString);
    }
}
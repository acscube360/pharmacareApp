package com.example.pharmacare.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pharmacare.R;
import com.example.pharmacare.adapter.CompletedOrderAdapter;
import com.example.pharmacare.model.IOrderDetailsSearch;
import com.example.pharmacare.model.Order;
import com.example.pharmacare.utility.Api;
import com.example.pharmacare.utility.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompleteOrderFragment extends Fragment implements IOrderDetailsSearch {
    private static final String TAG = "CompleteOrderFragment";
    ArrayList<Order> completedOrderArrayList;
    RecyclerView rv_completed_order;
    View view;
    public CompletedOrderAdapter adapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_complete_order, container, false);
        completedOrderArrayList = new ArrayList<>();
        rv_completed_order = view.findViewById(R.id.rv_completed_order);
        adapter = new CompletedOrderAdapter(getActivity(), completedOrderArrayList);
//     getCompletedOrderList();
        return view;
    }

    private void getCompletedOrderList() {
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
                            completedOrderArrayList.add(order);
//                        }
                    }
                    populateList(completedOrderArrayList);
                }else{
                    Log.e("error","error");
                }

                progressDialog.dismiss();
                Log.e(TAG, "completedOrderSize" + completedOrderArrayList.size());

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    private void populateList(ArrayList<Order> orders) {
        CompletedOrderAdapter adapter = new CompletedOrderAdapter(getActivity(), orders);
        rv_completed_order.setLayoutManager(new LinearLayoutManager(view.getContext()));
       // rv_completed_order.setLayoutManager(linearLayoutManager);
        rv_completed_order.setAdapter(adapter);
       // progressDialog.dismiss();
    }
    public void filterTransactions(String searchText) {
        if (rv_completed_order != null && rv_completed_order.getAdapter() != null && !searchText.isEmpty()) {
            // mAdapter.filterData(search_text);
            try {
//                ((CompletedOrderAdapter) rv_completed_order.getAdapter()).getFilter().filter(searchText);
                CompletedOrderAdapter adapter = this.adapter;
                adapter.getFilter().filter(searchText);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void onSearch(String inputString) {
        filterTransactions(inputString);
    }
}
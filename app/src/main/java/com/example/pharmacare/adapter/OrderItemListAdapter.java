package com.example.pharmacare.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacare.R;
import com.example.pharmacare.model.OrderItem;

import java.util.ArrayList;
import java.util.Objects;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.ViewHolder> {
    private ArrayList<OrderItem> orderItemArrayList;
    private Context context;

    public OrderItemListAdapter(Context context, ArrayList<OrderItem> orderItemArrayList) {
        this.orderItemArrayList = orderItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = orderItemArrayList.get(position);
        Log.e("itemPosition", String.valueOf(position));
        if (position % 2== 0) {
            holder.ll_item_bg.setBackgroundResource(R.color.order_list_item_bg);
        } else {
            holder.ll_item_bg.setBackgroundResource(R.color.white);

        }
        holder.tv_item_name.setText(orderItem.getName());
        holder.tv_selling_type.setText(orderItem.getSellingType().getName());
        holder.tv_quan.setText(String.valueOf(orderItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public ArrayList<OrderItem> getOrderItemArrayList() {
        return orderItemArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_item_name;
        final TextView tv_selling_type;
        final TextView tv_quan;
        final LinearLayout ll_item_bg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_selling_type = itemView.findViewById(R.id.tv_selling_type);
            tv_quan = itemView.findViewById(R.id.tv_quan);
            ll_item_bg = itemView.findViewById(R.id.ll_item_bg);


        }
    }
}

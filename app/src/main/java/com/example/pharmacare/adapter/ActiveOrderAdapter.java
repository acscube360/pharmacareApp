package com.example.pharmacare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacare.R;
import com.example.pharmacare.model.Order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActiveOrderAdapter extends RecyclerView.Adapter<ActiveOrderAdapter.ViewHolder> {
    private Context context;
    private List<Order> activeOrderList;

    public ActiveOrderAdapter(Context context, List<Order> item) {
        this.context = context;
        this.activeOrderList = item;
    }

    @NonNull
    @Override
    public ActiveOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_completed_order, parent, false);
        return new ActiveOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveOrderAdapter.ViewHolder holder, int position) {
        Order order = activeOrderList.get(position);
        try {
            holder.tv_date.setText((CharSequence) formatDate(order.getCreated()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_order_name.setText(order.getRemark());
        holder.tv_sup_by.setText(order.getLastModifiedBy());
    }

    private String formatDate(String strDate) throws ParseException {

        DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date da = (Date) inputFormatter.parse(strDate);


        DateFormat outputFormatter = new SimpleDateFormat("MM/dd");
        String strDateTime = outputFormatter.format(da);

        return strDateTime;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public Order getItem(int position) {
        return activeOrderList.get(position);
    }

    public void removeItem(int position) {
        activeOrderList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item, int position) {
        activeOrderList.add(position, item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tv_date;
        final TextView tv_order_name;
        final TextView tv_sup_by;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_order_name = itemView.findViewById(R.id.tv_order_name);
            tv_sup_by = itemView.findViewById(R.id.tv_sup_by);
        }

        public void removeItem(int position) {
            activeOrderList.remove(position);
            notifyItemRemoved(position);
        }
    }
}

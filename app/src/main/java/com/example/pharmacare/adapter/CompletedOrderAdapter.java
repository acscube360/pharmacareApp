package com.example.pharmacare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacare.R;
import com.example.pharmacare.model.Order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.ViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<Order> completedOrderList;
    private List<Order> originalItems = new ArrayList<>();
    private List<Order> filterItems = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();


    public CompletedOrderAdapter(Context context, ArrayList<Order> completedOrderList) {
        this.context = context;
        this.completedOrderList = completedOrderList;
    }

    public List<Order> getOriginalItems() {
        return originalItems;
    }

    public void setOriginalItems(List<Order> originalItems) {
        this.originalItems = originalItems;
    }

    @NonNull
    @Override
    public CompletedOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_completed_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderAdapter.ViewHolder holder, int position) {
        Order order = completedOrderList.get(position);
        try {
            holder.tv_date.setText((CharSequence) formatDate(order.getCreated()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_order_name.setText(order.getRemark());
        holder.tv_sup_by.setText(order.getLastModifiedBy());
    }

    @Override
    public int getItemCount() {
        return completedOrderList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tv_date;
        final TextView tv_order_name;
        final TextView tv_sup_by;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_order_name = itemView.findViewById(R.id.tv_order_name);
            tv_sup_by = itemView.findViewById(R.id.tv_sup_by);
        }
    }

    private String formatDate(String strDate) throws ParseException {

        DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date da = (Date) inputFormatter.parse(strDate);


        DateFormat outputFormatter = new SimpleDateFormat("MM/dd");
        String strDateTime = outputFormatter.format(da);

        return strDateTime;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                String[] tokens = constraint.toString().split(" ");
                constraint = constraint.toString().toUpperCase();
                ArrayList<Order> filters = new ArrayList<Order>();
                final List<Order> list = originalItems;
                //get specific items
                for (int i = 0; i < list.size(); i++) {
                    Order prod = list.get(i);
                    boolean isMatch = true;
//                    String prodNameUpper = (prod.getRemark() + " " + prod.getCode()).toUpperCase();
//
//                    for (String token : tokens) {
//
//                        if (!prodNameUpper.contains(token.toUpperCase())) {
//                            isMatch = false;
//                            break;
//                        }
//                    }

                    if (isMatch)
                        filters.add(prod);

                }

                results.count = filters.size();
                results.values = filters;

            } else {

                results.count = originalItems.size();
                results.values = originalItems;
            }
            return results;

        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterItems = (List<Order>) results.values;


            notifyDataSetChanged();
        }


    }
}



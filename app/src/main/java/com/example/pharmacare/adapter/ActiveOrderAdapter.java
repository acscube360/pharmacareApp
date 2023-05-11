package com.example.pharmacare.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacare.AddItemActivity;
import com.example.pharmacare.R;
import com.example.pharmacare.ShowOrderDetailsActivity;
import com.example.pharmacare.model.Order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActiveOrderAdapter extends RecyclerView.Adapter<ActiveOrderAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Order> activeOrderList;
    private List<Order> originalItems = new ArrayList<>();
    private List<Order> filterItems = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();

    public ActiveOrderAdapter(Context context, List<Order> item) {
        this.context = context;
        originalItems = item;
        filterItems = item;
    }

    @NonNull
    @Override
    public ActiveOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_completed_order, parent, false);
        return new ActiveOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveOrderAdapter.ViewHolder holder, int position) {
        Order order = filterItems.get(position);
        try {
            holder.tv_date.setText((CharSequence) formatDate(order.getCreated()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_order_name.setText(order.getRemark());
        holder.tv_sup_by.setText(order.getLastModifiedBy());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddItemActivity.class);
                intent.putExtra("item_code", "");
                intent.putExtra("order", order);
                intent.putExtra("fromActiveList",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                v.getContext().startActivity(intent);
            }
        });
    }

    public ArrayList<Order> getOrderList() {
        return (ArrayList<Order>) filterItems;
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
        return filterItems.size();
    }

    public Order getItem(int position) {
        return filterItems.get(position);
    }

    public void removeItem(int position) {
        filterItems.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item, int position) {
        filterItems.add(position, item);
        notifyItemInserted(position);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
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


    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String query = constraint.toString().toLowerCase();
            final List<Order> orders = originalItems;
            final List<Order> result_list = new ArrayList<>(orders.size());

            for (int i = 0; i < orders.size(); i++) {
                Log.e("i", String.valueOf(i));

                //String str_cat = list.get(i).getCategory();
//                Log.e("str_title",str_title);
                if (orders.get(i).getRemark() != null) {
                    String str_title = orders.get(i).getRemark();
                    if (str_title.toLowerCase().contains(query)) {
                        result_list.add(orders.get(i));
                        Log.e("added>>", orders.get(i).getRemark());
                    }
                }
            }

            results.values = result_list;
            results.count = result_list.size();
            //  notifyDataSetChanged();
            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.e("result>>", results.toString());
            filterItems = (ArrayList<Order>) results.values;


            notifyDataSetChanged();
        }


    }

}

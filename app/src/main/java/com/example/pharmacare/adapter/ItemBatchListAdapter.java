package com.example.pharmacare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacare.R;
import com.example.pharmacare.model.ItemBatch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemBatchListAdapter extends RecyclerView.Adapter<ItemBatchListAdapter.ViewHolder> {

    private ArrayList<ItemBatch> itemBatches;
    private Context context;

    public ItemBatchListAdapter(ArrayList<ItemBatch> itemBatches,Context context) {
        this.itemBatches = itemBatches;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_more, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ItemBatch itemBatch = itemBatches.get(position);
        holder.tv_batch_no.setText(itemBatch.getBatchName() );
        holder.tv_avail_q.setText(String.valueOf(itemBatch.getStock()));
        try {
            holder.tv_exp_date.setText(formatDate(itemBatch.getExpiryDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_rec_q.setText(String.valueOf(itemBatch.getReceivedStock()));
        holder.tv_unit_cost.setText(String.valueOf(itemBatch.getUnitCost()));
        holder.tv_unit_price.setText(String.valueOf(itemBatch.getUnitPrice()));


    }

    @Override
    public int getItemCount() {
        return itemBatches.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_batch_no, tv_avail_q, tv_exp_date, tv_rec_q, tv_unit_cost, tv_unit_price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_batch_no = itemView.findViewById(R.id.tv_batch_no);
            tv_avail_q = itemView.findViewById(R.id.tv_avail_q);
            tv_exp_date = itemView.findViewById(R.id.tv_exp_date);
            tv_rec_q = itemView.findViewById(R.id.tv_rec_q);
            tv_unit_cost = itemView.findViewById(R.id.tv_unit_cost);
            tv_unit_price = itemView.findViewById(R.id.tv_unit_price);


        }
    }

    private String formatDate(String strDate) throws ParseException {

        DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date da = (Date) inputFormatter.parse(strDate);


//        DateFormat outputFormatter = new SimpleDateFormat("MM/dd");
        String strDateTime = inputFormatter.format(da);

        return strDateTime;
    }
}

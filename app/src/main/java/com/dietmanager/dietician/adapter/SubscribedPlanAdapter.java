package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.subscriptionplan.SubscriptionPlanItem;

import java.util.List;

public class SubscribedPlanAdapter extends RecyclerView.Adapter<SubscribedPlanAdapter.MyViewHolder> {

    private Context context;
    private List<SubscriptionPlanItem> list;

    public SubscribedPlanAdapter(List<SubscriptionPlanItem> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscribe_plan_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        SubscriptionPlanItem item = list.get(position);
        holder.tvPlanName.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(Order item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setList(List<SubscriptionPlanItem> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlanName;

        public MyViewHolder(View view) {
            super(view);
            tvPlanName = view.findViewById(R.id.tvPlanName);
        }
    }
}
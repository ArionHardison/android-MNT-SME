package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.subscriptionplan.SubscriptionPlanItem;

import java.util.List;

public class SubscribedPlanAdapter extends RecyclerView.Adapter<SubscribedPlanAdapter.MyViewHolder> {

    private Context context;
    private ISubscriptionPlanListener listener;
    private List<SubscriptionPlanItem> list;

    public SubscribedPlanAdapter(List<SubscriptionPlanItem> list, Context con,ISubscriptionPlanListener listener) {
        this.list = list;
        this.context = con;
        this.listener = listener;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSubscriptionPlanClicked(item);
            }
        });
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
        LinearLayout itemView;

        public MyViewHolder(View view) {
            super(view);
            tvPlanName = view.findViewById(R.id.tvPlanName);
            itemView = view.findViewById(R.id.item_view);
        }
    }

    public interface ISubscriptionPlanListener{
        void onSubscriptionPlanClicked(SubscriptionPlanItem subscriptionPlanItem);
    }
}
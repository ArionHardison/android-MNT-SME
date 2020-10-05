package com.dietmanager.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.activity.OrderDetailActivity;
import com.dietmanager.restaurant.helper.GlobalData;
import com.dietmanager.restaurant.model.Order;
import com.dietmanager.restaurant.utils.Utils;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private List<Order> list;

    public HistoryAdapter(List<Order> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Order order = list.get(position);
        if (order.getAddress() != null) {
            if (order.getAddress().getMapAddress() != null) {
                holder.address.setText((order.getAddress().getBuilding() != null ? order.getAddress().getBuilding() + ", " : "") +
                        order.getAddress().getMapAddress());
            }
        } else {
            if (order.getShop().getMapsAddress() != null) {
                holder.address.setText(order.getShop().getMapsAddress());
            }
        }
        holder.price.setText(/*context.getString(R.string.currency_value)*/GlobalData.profile.getCurrency() + "" + order.getInvoice().getPayable());

        //Default Status and color
        String status = context.getResources().getString(R.string.dispute_created);
        holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorRed));

        if (order.getDispute().equalsIgnoreCase("CREATED")) {
            holder.status.setText(status);
        } else if (order.getStatus().equals("CANCELLED") || order.getStatus().equals("COMPLETED")) {
            status = "";
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.status.setText(status);

        } else {
            status = context.getResources().getString(R.string.status_ongoing);
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.status.setText(status);
        }
        if (!order.getStatus().equals("CANCELLED") && !order.getStatus().equals("COMPLETED")) {
            if (order.getScheduleStatus() != null) {
                if (order.getScheduleStatus() == 1) {
                    holder.tvScheduleStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.tvScheduleStatus.setVisibility(View.GONE);
                }
            } else {
                holder.tvScheduleStatus.setVisibility(View.GONE);
            }
        } else {
            holder.tvScheduleStatus.setVisibility(View.GONE);
        }
        if (order.getUser() != null) {
            String name = Utils.toFirstCharUpperAll(order.getUser().getName());
            holder.userName.setText(name);
            Glide.with(context).load(order.getUser().getAvatar())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image).error(R.drawable.ic_place_holder_image).dontAnimate()).into(holder.userImg);
        }
        String payment_mode;
        if (order.getInvoice().getPaymentMode().equalsIgnoreCase("stripe")) {
            payment_mode = context.getString(R.string.credit_card);
        } else {
            payment_mode = Utils.toFirstCharUpperAll(order.getInvoice().getPaymentMode());
        }
        holder.paymentMode.setText(payment_mode);
        holder.itemLayout.setOnClickListener(v -> {
            GlobalData.selectedOrder = list.get(position);
            context.startActivity(new Intent(context, OrderDetailActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(Order item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Order item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address, paymentMode, price, status, tvScheduleStatus;
        CardView itemLayout;
        ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            price = view.findViewById(R.id.price);
            address = view.findViewById(R.id.address);
            paymentMode = view.findViewById(R.id.payment_mode);
            status = view.findViewById(R.id.status);
            tvScheduleStatus = view.findViewById(R.id.tvScheduleStatus);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
        }
    }
}
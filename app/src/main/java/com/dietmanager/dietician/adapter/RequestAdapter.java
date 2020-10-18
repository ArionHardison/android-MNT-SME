package com.dietmanager.dietician.adapter;

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
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.activity.RequestAcceptActivity;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.utils.Utils;

import java.text.ParseException;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    Context context;
    private List<Order> list;

    public RequestAdapter(List<Order> list, Context con) {
        this.list = list;
        this.context = con;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list_item, parent, false);

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
        try {
            if (order.getCreatedAt() != null) {
                holder.orderDate.setText(Utils.getDate(order.getCreatedAt()));
                holder.orderTime.setText(Utils.getTime(order.getCreatedAt()));
            }
            if (order.getScheduleStatus() != null) {
                if (order.getScheduleStatus() == 1) {
                    holder.tvScheduleLabel.setVisibility(View.VISIBLE);
                    holder.scehduleDate.setVisibility(View.VISIBLE);
                    holder.scehduleTime.setVisibility(View.VISIBLE);
                    holder.scehduleDate.setText(Utils.getDate(order.getDeliveryDate()));
                    holder.scehduleTime.setText(Utils.getTime(order.getDeliveryDate()));
                } else {
                    holder.tvScheduleLabel.setVisibility(View.GONE);
                    holder.scehduleDate.setVisibility(View.GONE);
                    holder.scehduleTime.setVisibility(View.GONE);
                }
            } else {
                holder.tvScheduleLabel.setVisibility(View.GONE);
                holder.scehduleDate.setVisibility(View.GONE);
                holder.scehduleTime.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (order.getPickUpRestaurant() != null) {
            if (order.getPickUpRestaurant() == 0) {
                holder.orderType.setText(context.getString(R.string.order_type_delivery));
            } else if (order.getPickUpRestaurant() == 1) {
                holder.orderType.setText(context.getString(R.string.order_type_takeaway));
                holder.address.setVisibility(View.GONE);
            } else {
                holder.orderType.setText(context.getString(R.string.order_type_delivery));
            }
        }
        //Default Status and color
        String status = "";
        holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        if (order.getStatus().equals("ORDERED") && order.getDispute().equals("NODISPUTE")) {
            status = context.getResources().getString(R.string.incoming);
        } else if (order.getStatus().equals("RECEIVED")) {
            status = "Processing";
        } else {
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
            status = context.getResources().getString(R.string.dispute_created);
        }
        holder.status.setText(status);

        String name = Utils.toFirstCharUpperAll(order.getUser().getName());
        String payment_mode;
        if (order.getInvoice().getPaymentMode().equalsIgnoreCase("stripe")) {
            payment_mode = context.getString(R.string.credit_card);
        } else {
            payment_mode = Utils.toFirstCharUpperAll(order.getInvoice().getPaymentMode());
        }
        holder.userName.setText(name);
        holder.paymentMode.setText(payment_mode);

        holder.itemLayout.setOnClickListener(v -> {
            GlobalData.selectedOrder = list.get(position);
            context.startActivity(new Intent(context, RequestAcceptActivity.class));
        });
        Glide.with(context).load(order.getUser().getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.delete_shop).error(R.drawable.delete_shop).dontAnimate()).into(holder.userImg);

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

        TextView userName, address, paymentMode, orderTime, orderDate, tvScheduleLabel,
                scehduleTime, scehduleDate, status, orderType;
        CardView itemLayout;
        ImageView userImg;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            orderDate = view.findViewById(R.id.order_date);
            orderTime = view.findViewById(R.id.order_time);
            tvScheduleLabel = view.findViewById(R.id.tvScheduleLabel);
            scehduleDate = view.findViewById(R.id.schedule_date);
            scehduleTime = view.findViewById(R.id.schedule_time);
            address = view.findViewById(R.id.address);
            paymentMode = view.findViewById(R.id.payment_mode);
            status = view.findViewById(R.id.status);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
            orderType = view.findViewById(R.id.order_type);
        }
    }

}

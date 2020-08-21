package com.oyola.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oyola.restaurant.R;
import com.oyola.restaurant.activity.TakeAwayActivity;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.model.SectionHeaderItem;
import com.oyola.restaurant.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanth on 29-10-2019.
 */
public class TakeAwayAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SectionHeaderItem> requestItemList;

    public TakeAwayAdapter(Context context) {
        this.context = context;
        requestItemList = new ArrayList<>();
    }

    public void setRequestItemList(List<SectionHeaderItem> itemList) {
        if (itemList == null) {
            return;
        }

        this.requestItemList.clear();
        this.requestItemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = LayoutInflater.from(context).inflate(R.layout.item_ongoing_header, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_takeway, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int section) {
        SectionHeaderItem item = requestItemList.get(section);
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
        headerViewHolder.setHeaderDataToView(item.getSectionTitle());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int section, int position, int absolutePosition) {
        List<Order> orderList = requestItemList.get(section).getOrderList();
        Order order = orderList.get(position);

        ItemViewHolder holder = (ItemViewHolder) viewHolder;

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
            holder.orderDate.setText(Utils.getDate(order.getCreatedAt()));
            holder.orderTime.setText(Utils.getTime(order.getCreatedAt()));
            holder.orderDeliveryDate.setText(Utils.getDate(order.getDeliveryDate()));
            holder.orderDeliveryTime.setText(Utils.getTime(order.getDeliveryDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (order.getScheduleStatus() != null) {
            if (order.getScheduleStatus() == 1) {
                holder.mLayoutSchedule.setVisibility(View.VISIBLE);
                holder.tvScheduleStatus.setVisibility(View.VISIBLE);
            } else {
                holder.mLayoutSchedule.setVisibility(View.GONE);
                holder.tvScheduleStatus.setVisibility(View.GONE);
            }
        } else {
            holder.mLayoutSchedule.setVisibility(View.GONE);
            holder.tvScheduleStatus.setVisibility(View.GONE);
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
        String status = context.getResources().getString(R.string.dispute_created);
        holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorRed));

        if (order.getStatus().equals("ORDERED") && order.getDispute().equals("NODISPUTE")) {
            status = context.getResources().getString(R.string.incoming);
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
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
            GlobalData.selectedOrder = order;
            context.startActivity(new Intent(context, TakeAwayActivity.class));
        });
        Glide.with(context).load(order.getUser().getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.delete_shop).error(R.drawable.delete_shop).dontAnimate()).into(holder.userImg);
    }

    @Override
    public int getSectionCount() {
        return requestItemList.size();
    }

    @Override
    public int getItemCount(int section) {
        return requestItemList.get(section).getOrderList().size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tv_ongoing_header);
        }

        public void setHeaderDataToView(String headerTitle) {
            tvHeader.setText(headerTitle);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView userName, address, paymentMode, orderTime, orderDate, orderDeliveryDate,
                orderDeliveryTime, status, orderType, tvScheduleStatus;
        CardView itemLayout;
        LinearLayout mLayoutSchedule;
        ImageView userImg;

        public ItemViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            orderDate = view.findViewById(R.id.order_date);
            orderTime = view.findViewById(R.id.order_time);
            address = view.findViewById(R.id.address);
            paymentMode = view.findViewById(R.id.payment_mode);
            status = view.findViewById(R.id.status);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
            orderType = view.findViewById(R.id.order_type);
            tvScheduleStatus = view.findViewById(R.id.tvScheduleStatus);
            orderDeliveryDate = view.findViewById(R.id.delivery_date);
            orderDeliveryTime = view.findViewById(R.id.order_delivery_time);
            mLayoutSchedule = view.findViewById(R.id.lay_schedule_detail);
        }
    }
}
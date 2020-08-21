package com.oyola.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oyola.restaurant.R;
import com.oyola.restaurant.activity.RequestAcceptActivity;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.model.SectionHeaderItem;
import com.oyola.restaurant.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RequestHeaderAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SectionHeaderItem> requestItemList;

    public RequestHeaderAdapter(Context context) {
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
            View itemView = LayoutInflater.from(context).inflate(R.layout.request_list_item, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public int getSectionCount() {
        return requestItemList.size();
    }

    @Override
    public int getItemCount(int section) {
        return requestItemList.get(section).getOrderList().size();
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
        holder.bindDataToViews(order);
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

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView userName, address, paymentMode, orderTime, orderDate, tvScheduleLabel,
                scehduleTime, scehduleDate, tvStatus, orderType;
        private CardView itemLayout;
        private ImageView userImg;

        public ItemViewHolder(@NonNull View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            orderDate = view.findViewById(R.id.order_date);
            orderTime = view.findViewById(R.id.order_time);
            tvScheduleLabel = view.findViewById(R.id.tvScheduleLabel);
            scehduleDate = view.findViewById(R.id.schedule_date);
            scehduleTime = view.findViewById(R.id.schedule_time);
            address = view.findViewById(R.id.address);
            paymentMode = view.findViewById(R.id.payment_mode);
            tvStatus = view.findViewById(R.id.status);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
            orderType = view.findViewById(R.id.order_type);
        }

        public void bindDataToViews(Order order) {
            if (order.getAddress() != null) {
                if (order.getAddress().getMapAddress() != null) {
                    address.setText((order.getAddress().getBuilding() != null ? order.getAddress().getBuilding() + ", " : "") +
                            order.getAddress().getMapAddress());
                }
            } else {
                if (order.getShop().getMapsAddress() != null) {
                    address.setText(order.getShop().getMapsAddress());
                }
            }
            try {
                if (order.getCreatedAt() != null) {
                    orderDate.setText(Utils.getDate(order.getCreatedAt()));
                    orderTime.setText(Utils.getTime(order.getCreatedAt()));
                }
                if (order.getScheduleStatus() != null) {
                    if (order.getScheduleStatus() == 1) {
                        tvScheduleLabel.setVisibility(View.VISIBLE);
                        scehduleDate.setVisibility(View.VISIBLE);
                        scehduleTime.setVisibility(View.VISIBLE);
                        scehduleDate.setText(Utils.getDate(order.getDeliveryDate()));
                        scehduleTime.setText(Utils.getTime(order.getDeliveryDate()));
                    } else {
                        tvScheduleLabel.setVisibility(View.GONE);
                        scehduleDate.setVisibility(View.GONE);
                        scehduleTime.setVisibility(View.GONE);
                    }
                } else {
                    tvScheduleLabel.setVisibility(View.GONE);
                    scehduleDate.setVisibility(View.GONE);
                    scehduleTime.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (order.getPickUpRestaurant() != null) {
                if (order.getPickUpRestaurant() == 0) {
                    orderType.setText(context.getString(R.string.order_type_delivery));
                } else if (order.getPickUpRestaurant() == 1) {
                    orderType.setText(context.getString(R.string.order_type_takeaway));
                    address.setVisibility(View.GONE);
                } else {
                    orderType.setText(context.getString(R.string.order_type_delivery));
                }
            }
            //Default Status and color
            String status = "";
            tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            if (order.getStatus().equals("ORDERED") && order.getDispute().equals("NODISPUTE")) {
                status = context.getResources().getString(R.string.incoming);
            } else if (order.getStatus().equals("RECEIVED")) {
                status = "Processing";
            } else {
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                status = context.getResources().getString(R.string.dispute_created);
            }
            tvStatus.setText(status);

            String name = Utils.toFirstCharUpperAll(order.getUser().getName());
            String payment_mode;
            if (order.getInvoice().getPaymentMode().equalsIgnoreCase("stripe")) {
                payment_mode = context.getString(R.string.credit_card);
            } else {
                payment_mode = Utils.toFirstCharUpperAll(order.getInvoice().getPaymentMode());
            }
            userName.setText(name);
            paymentMode.setText(payment_mode);

            itemLayout.setOnClickListener(v -> {
                GlobalData.selectedOrder = order;
                context.startActivity(new Intent(context, RequestAcceptActivity.class));
            });
            Glide.with(context).load(order.getUser().getAvatar())
                    .apply(new RequestOptions().placeholder(R.drawable.delete_shop).error(R.drawable.delete_shop).dontAnimate()).into(userImg);
        }

    }

}

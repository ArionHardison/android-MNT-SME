package com.dietmanager.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.activity.RequestAcceptActivity;
import com.dietmanager.restaurant.helper.GlobalData;
import com.dietmanager.restaurant.model.OngoingHistoryModel;
import com.dietmanager.restaurant.model.Order;
import com.dietmanager.restaurant.utils.TextUtils;
import com.dietmanager.restaurant.utils.Utils;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.List;

public class IncomingStickyAdapter extends SectioningAdapter {

    public static MediaPlayer mPlayer;
    private Context context;
    private LayoutInflater inflater;
    private List<OngoingHistoryModel> ongoingHistoryList;


    public IncomingStickyAdapter(Context context) {
        this.context = context;
        ongoingHistoryList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        mPlayer = MediaPlayer.create(context, R.raw.alert_tone);
        mPlayer.isLooping();
    }

    public void setStickyItemList(List<OngoingHistoryModel> itemList) {
        if (itemList == null) {
            return;
        }
        ongoingHistoryList.clear();
        ongoingHistoryList.addAll(itemList);
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return !TextUtils.isEmpty(ongoingHistoryList.get(sectionIndex).getHeaderName());
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        View view = inflater.inflate(R.layout.item_ongoing_header, parent, false);
        return new ItemHeaderViewHolder(view);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        View view = inflater.inflate(R.layout.item_incoming, parent, false);
        return new ItemChildViewHolder(view);
    }

    @Override
    public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
        final View ghostView = new View(parent.getContext());
        ghostView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new GhostHeaderViewHolder(ghostView);
    }

    @Override
    public int getNumberOfSections() {
        return ongoingHistoryList.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return ongoingHistoryList.get(sectionIndex).getOrderList().size();
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex,
                                       int headerUserType) {
        super.onBindHeaderViewHolder(viewHolder, sectionIndex, headerUserType);

        ItemHeaderViewHolder holder = (ItemHeaderViewHolder) viewHolder;

        OngoingHistoryModel ongoingHistoryModel = ongoingHistoryList.get(sectionIndex);

        holder.setHeaderDataToView(ongoingHistoryModel);
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex,
                                     int itemUserType) {
        super.onBindItemViewHolder(viewHolder, sectionIndex, itemIndex, itemUserType);

        ItemChildViewHolder holder = (ItemChildViewHolder) viewHolder;

        Order order = ongoingHistoryList.get(sectionIndex).getOrderList().get(itemIndex);

        holder.setChildDataToView(order);
    }

    @Override
    public void onBindGhostHeaderViewHolder(GhostHeaderViewHolder viewHolder,
                                            int sectionIndex) {
        viewHolder.itemView.setBackgroundColor(0xFF9999FF);
    }

    class ItemHeaderViewHolder extends HeaderViewHolder {

        private TextView tvHeader;

        public ItemHeaderViewHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tv_ongoing_header);
        }

        public void setHeaderDataToView(OngoingHistoryModel item) {
            tvHeader.setText(item.getHeaderName());
        }
    }

    class ItemChildViewHolder extends ItemViewHolder {

        private TextView userName, tvAddress, paymentMode, tvOrderTime, tvScheduleTime, tvStatus, orderType;
        private Group groupScheduleTime;
        private RelativeLayout itemLayout;
        private ImageView userImg;

        public ItemChildViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.tv_user_name);
            tvOrderTime = view.findViewById(R.id.tv_order_time);
            groupScheduleTime = view.findViewById(R.id.group_schedule_time);
            tvScheduleTime = view.findViewById(R.id.tv_schedule_time);
            tvAddress = view.findViewById(R.id.tv_address);
            paymentMode = view.findViewById(R.id.tv_payment_mode);
            tvStatus = view.findViewById(R.id.tv_status);
            itemLayout = view.findViewById(R.id.layout_incoming);
            userImg = view.findViewById(R.id.img_user);
            orderType = view.findViewById(R.id.tv_order_type);
        }

        public void setChildDataToView(Order order) {
            String address;
            if (order.getAddress() != null && order.getAddress().getMapAddress() != null) {
                address = (order.getAddress().getBuilding() != null ? order.getAddress().getBuilding() + ", " : "") +
                        order.getAddress().getMapAddress();
            } else {
                address = (order.getShop() != null && order.getShop().getMapsAddress() != null) ? order.getShop().getMapsAddress() : "";
            }
            tvAddress.setText(!TextUtils.isEmpty(address) ? address : "NA");
            if (!TextUtils.isEmpty(order.getCreatedAt())) {
                String orderTime = Utils.getOrderScheduleDateAndTime(order.getCreatedAt());
                tvOrderTime.setText(!TextUtils.isEmpty(orderTime) ? orderTime : "NA");
            }
            if (order.getScheduleStatus() != null) {
                if (order.getScheduleStatus() == 1) {
                    groupScheduleTime.setVisibility(View.VISIBLE);
                    tvScheduleTime.setVisibility(View.VISIBLE);

                    String scheduleTime = !TextUtils.isEmpty(order.getDeliveryDate()) ? Utils.getOrderScheduleDateAndTime(order.getDeliveryDate()) : "NA";
                    tvScheduleTime.setText(!TextUtils.isEmpty(scheduleTime) ? scheduleTime : "NA");
                } else {
                    groupScheduleTime.setVisibility(View.GONE);
                    tvScheduleTime.setVisibility(View.GONE);
                }
            } else {
                groupScheduleTime.setVisibility(View.GONE);
                tvScheduleTime.setVisibility(View.GONE);
            }

            if (order.getPickUpRestaurant() != null) {
                if (order.getPickUpRestaurant() == 0) {
                    orderType.setText(context.getString(R.string.incoming_order_type_delivery));
                } else if (order.getPickUpRestaurant() == 1) {
                    orderType.setText(context.getString(R.string.incoming_order_type_takeaway));
                    tvAddress.setVisibility(View.GONE);
                } else {
                    orderType.setText(context.getString(R.string.incoming_order_type_delivery));
                }
            }
            //Default Status and color
            String status = "";
            tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            if (order.getStatus().equals("ORDERED") && order.getDispute().equals("NODISPUTE")) {
                status = context.getResources().getString(R.string.incoming);
                mPlayer.start();
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
                mPlayer.stop();
                context.startActivity(new Intent(context, RequestAcceptActivity.class));
            });
            Glide.with(context).load(order.getUser().getAvatar())
                    .apply(new RequestOptions().placeholder(R.drawable.delete_shop).error(R.drawable.delete_shop).dontAnimate()).into(userImg);
        }
    }
}

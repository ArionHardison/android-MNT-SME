package com.oyola.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oyola.restaurant.R;
import com.oyola.restaurant.activity.OrderDetailActivity;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.OngoingHistoryModel;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.utils.TextUtils;
import com.oyola.restaurant.utils.Utils;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnGoingStickyAdapter extends SectioningAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<OngoingHistoryModel> ongoingHistoryList;

    public OnGoingStickyAdapter(Context context) {
        this.context = context;
        ongoingHistoryList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
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
        View view = inflater.inflate(R.layout.item_order_history, parent, false);
        return new ItemChildViewHolder(view);
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

    class ItemChildViewHolder extends SectioningAdapter.ItemViewHolder {

        private TextView userName, address, paymentMode, price, tvStatus;
        private CardView itemLayout;
        private ImageView userImg;

        public ItemChildViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.user_name);
            price = view.findViewById(R.id.price);
            address = view.findViewById(R.id.address);
            paymentMode = view.findViewById(R.id.payment_mode);
            tvStatus = view.findViewById(R.id.status);
            itemLayout = view.findViewById(R.id.item_layout);
            userImg = view.findViewById(R.id.user_img);
        }

        public void setChildDataToView(Order order) {
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
            price.setText(/*context.getString(R.string.currency_value)*/
                    GlobalData.profile.getCurrency() + "" + order.getInvoice().getPayable());

            //Default Status and color
            String status = context.getResources().getString(R.string.dispute_created);
            tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed));

            if (order.getDispute().equalsIgnoreCase("CREATED")) {
                tvStatus.setText(status);
            } else if (order.getStatus().equals("CANCELLED") || order.getStatus().equals("COMPLETED")) {
                status = "";
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                tvStatus.setText(status);

            } else {
                status = context.getResources().getString(R.string.status_ongoing);
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                tvStatus.setText(status);
            }

            if (order.getUser() != null) {
                String name = Utils.toFirstCharUpperAll(order.getUser().getName());
                userName.setText(name);
                Glide.with(context).load(order.getUser().getAvatar())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image).error(R.drawable.ic_place_holder_image).dontAnimate()).into(userImg);
            }
            String payment_mode;
            if (order.getInvoice().getPaymentMode().equalsIgnoreCase("stripe")) {
                payment_mode = context.getString(R.string.credit_card);
            } else {
                payment_mode = Utils.toFirstCharUpperAll(order.getInvoice().getPaymentMode());
            }
            paymentMode.setText(payment_mode);
            itemLayout.setOnClickListener(v -> {
                GlobalData.selectedOrder = order;
                context.startActivity(new Intent(context, OrderDetailActivity.class));
            });
        }
    }
}

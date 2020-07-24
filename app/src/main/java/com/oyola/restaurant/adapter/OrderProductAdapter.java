package com.oyola.restaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.oyola.restaurant.R;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.AddonProduct;
import com.oyola.restaurant.model.CartAddon;
import com.oyola.restaurant.model.Item;
import com.oyola.restaurant.utils.TextUtils;

import java.util.List;

public class OrderProductAdapter extends SectionedRecyclerViewAdapter<OrderProductAdapter.ViewHolder> {

    private Context context;
    private List<Item> list;
    private LayoutInflater inflater;

    public OrderProductAdapter(Context context, List<Item> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.order_product_header, parent, false);
                return new ViewHolder(v, true);
            case VIEW_TYPE_ITEM:
            default:
                v = inflater.inflate(R.layout.order_addons_list_item, parent, false);
                return new ViewHolder(v, false);
        }
    }

    @Override
    public int getSectionCount() {
        return list.size();
    }

    @Override
    public int getItemCount(int section) {
        if (list.get(section).getCartAddons().isEmpty())
            return 1;
        else
            return list.get(section).getCartAddons().size();
    }

    @Override
    public void onBindHeaderViewHolder(OrderProductAdapter.ViewHolder holder, final int section) {
        Item item = list.get(section);
        String value = context.getResources().getString(R.string.product_, item.getProduct().getName(), item.getQuantity(), GlobalData.profile.getCurrency() + /*MyApplication.getNumberFormat().format(*/item.getProduct().getPrices().getOrignalPrice())/*)*/;
        holder.productDetail.setText(value);
        double totalAmount = /*Double.valueOf(*/item.getQuantity() * item.getProduct().getPrices().getOrignalPrice();
        holder.productPrice.setText(GlobalData.profile.getCurrency() +/*MyApplication.getNumberFormat().format(*/totalAmount)/*)*/;
        if (item.getProduct().getNote() != null) {
            holder.tvNotes.setVisibility(View.VISIBLE);
            holder.tvNotes.setText(item.getProduct().getNote());
        } else {
            holder.tvNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        if (!list.get(section).getCartAddons().isEmpty()) {
            CartAddon cartAddon = list.get(section).getCartAddons().get(relativePosition);
            AddonProduct addonProduct = cartAddon.getAddonProduct();
            double price = (addonProduct != null && addonProduct.getPrice() != null) ? addonProduct.getPrice() : 0;
            String name = (addonProduct != null && addonProduct.getAddon() != null && !TextUtils.isEmpty(addonProduct.getAddon().getName())) ?
                    addonProduct.getAddon().getName() : "";

            holder.itemLayout.setVisibility(View.VISIBLE);
            String value = context.getString(R.string.addon_, name,
                    list.get(section).getQuantity() * cartAddon.getQuantity(),
                    GlobalData.profile.getCurrency() + price);
            holder.addonDetail.setText(value);
            double totalAmount = price * list.get(section).getQuantity() * cartAddon.getQuantity();
            String currency = (GlobalData.profile != null && !TextUtils.isEmpty(GlobalData.profile.getCurrency())) ?
                    GlobalData.profile.getCurrency() :"";
            holder.addonPrice.setText(currency + totalAmount);
        } else {
            holder.itemLayout.setVisibility(View.GONE);
        }
    }

    private String getDetail(Integer quantity, int price) {
        String data = "( " + quantity +
                "x " +
                GlobalData.profile.getCurrency() +
                price +
                ")";
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productDetail;
        TextView productPrice;
        TextView tvNotes;
        TextView addonDetail;
        TextView addonPrice;
        LinearLayout itemLayout;

        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);
            if (isHeader) {
                productDetail = itemView.findViewById(R.id.product_detail);
                productPrice = itemView.findViewById(R.id.product_price);
                tvNotes = itemView.findViewById(R.id.tvNotes);
            } else {
                addonPrice = itemView.findViewById(R.id.addon_price);
                addonDetail = itemView.findViewById(R.id.addon_detail);
                itemLayout = itemView.findViewById(R.id.item_layout);
            }
        }
    }
}
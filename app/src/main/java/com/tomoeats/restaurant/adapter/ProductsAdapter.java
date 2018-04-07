package com.tomoeats.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.model.CategoryList;
import com.tomoeats.restaurant.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends SectionedRecyclerViewAdapter<ProductsAdapter.ViewHolder> {

    private List<CategoryList> list = new ArrayList<>();
    private LayoutInflater inflater;
    Context context;

    public ProductsAdapter(Context context, List<CategoryList> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.category_header, parent, false);
                return new ViewHolder(v, true);
            case VIEW_TYPE_ITEM:
                v = inflater.inflate(R.layout.product_list_item, parent, false);
                return new ViewHolder(v, false);
            default:
                v = inflater.inflate(R.layout.product_list_item, parent, false);
                return new ViewHolder(v, false);
        }
    }

    @Override
    public int getSectionCount() {
        return list.size();
    }


    @Override
    public int getItemCount(int section) {
        return list.get(section).getProductList().size();
    }

    @Override
    public void onBindHeaderViewHolder(ProductsAdapter.ViewHolder holder, final int section) {
        holder.header.setText(list.get(section).getHeader());
        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(list.get(section).getHeader());
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        Product object = list.get(section).getProductList().get(relativePosition);
        holder.productName.setText(object.getName());
//        if (object.getAddOns().equalsIgnoreCase("")) {
//            holder.addOns.setVisibility(View.GONE);
//            holder.noAddOns.setVisibility(View.VISIBLE);
//        } else {
//            holder.addOns.setVisibility(View.VISIBLE);
//            holder.noAddOns.setVisibility(View.GONE);
//        }
//        System.out.println(object.getName());
////        Glide.with(context).load(object.getShop().getAvatar()).into(holder.productImg);
//        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView noAddOns;
        TextView addOns;
        TextView productName;
        ImageView productImg;
        RelativeLayout itemLayout;

        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);
            if (isHeader) {
                header = (TextView) itemView.findViewById(R.id.category_header);
            } else {
                itemLayout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
                productName = (TextView) itemView.findViewById(R.id.product_name_txt);
                noAddOns = (TextView) itemView.findViewById(R.id.no_addons_txt);
                addOns = (TextView) itemView.findViewById(R.id.addons_name_txt);
                productImg = (ImageView) itemView.findViewById(R.id.product_img);
            }

        }


    }
}
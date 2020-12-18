package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    private Context context;
    private IIngredientListener listener;
    private List<IngredientsItem> list;

    public IngredientsAdapter(List<IngredientsItem> list, Context con, IIngredientListener listener) {
        this.list = list;
        this.context = con;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        IngredientsItem item = list.get(position);
        holder.tvName.setText(item.getName());
        holder.tvUnitType.setText(item.getUnitType().getName());
        holder.tvPrice.setText(GlobalData.profile.getCurrency() +item.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onIngredientClicked(item);
            }
        });
        Glide.with(context)
                .load(AppConfigure.BASE_URL + item.getAvatar())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.shimmer_bg)
                        .error(R.drawable.shimmer_bg))
                .into(holder.ingredientImg);
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

    public void setList(List<IngredientsItem> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvUnitType,tvPrice;
        LinearLayout itemView;
        ImageView ingredientImg;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvUnitType = view.findViewById(R.id.tvUnitType);
            ingredientImg = view.findViewById(R.id.ingredient_img);
            tvPrice = view.findViewById(R.id.tvPrice);
            itemView = view.findViewById(R.id.item_layout);
        }
    }

    public interface IIngredientListener {
        void onIngredientClicked(IngredientsItem ingredientsItem);
    }
}
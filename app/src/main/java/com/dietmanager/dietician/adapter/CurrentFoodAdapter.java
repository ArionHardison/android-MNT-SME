package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.model.currentfood.CurrentFoodItem;
import com.dietmanager.dietician.model.food.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class CurrentFoodAdapter extends RecyclerView.Adapter<CurrentFoodAdapter.MyViewHolder> {
    private List<CurrentFoodItem> foodItems;
    private Context context;

    public CurrentFoodAdapter(Context context) {
        foodItems = new ArrayList<>();
        this.context=context;
    }

    public void setList(List<CurrentFoodItem> itemList) {
        if (itemList == null) {
            return;
        }
        foodItems.clear();
        foodItems.addAll(itemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrentFoodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_food_list_item, parent, false);

        return new CurrentFoodAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentFoodAdapter.MyViewHolder holder, final int position) {
        CurrentFoodItem foodItem = foodItems.get(position);
        holder.tvFoodTitle.setText(String.valueOf(foodItem.getFood().getName()));
        holder.tvFoodDescription.setText(String.valueOf(foodItem.getFood().getDescription()));
        holder.tvFoodPrice.setText(String.valueOf(foodItem.getFood().getPrice()));
        holder.tvCarb.setText(foodItem.getFood().getCarbohydrates());
        holder.tvProtein.setText(foodItem.getFood().getProtein());
        holder.tvFat.setText(foodItem.getFood().getFat());

        String imgUrl="";
        if(foodItem.getFood().getAvatar()!=null)
            imgUrl=foodItem.getFood().getAvatar();
        Glide.with(context).load(AppConfigure.BASE_URL+imgUrl).apply(new RequestOptions().centerCrop().placeholder(R.drawable.shimmer_bg).error(R.drawable.shimmer_bg).dontAnimate()).into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoodTitle,tvFoodDescription,tvFoodPrice,tvProtein,tvFat,tvCarb;
        CardView cardItem;
        ImageView imgFood;

        MyViewHolder(View view) {
            super(view);
            imgFood = view.findViewById(R.id.img_food);
            tvFoodTitle = view.findViewById(R.id.tv_food_title);
            tvFoodPrice = view.findViewById(R.id.tv_food_price);
            tvFoodDescription = view.findViewById(R.id.tv_food_description);
            tvProtein = view.findViewById(R.id.tv_proteins);
            tvFat = view.findViewById(R.id.tv_fat);
            tvCarb = view.findViewById(R.id.tv_carb);
            cardItem = view.findViewById(R.id.card_item);
        }
    }
}
package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.model.food.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> implements Filterable {
    private List<FoodItem> foodItems =new ArrayList<>();
    private List<FoodItem> filterItems =new ArrayList<>();
    private Context context;
    private IFoodListener listener;

    public FoodAdapter(Context context,IFoodListener listener) {
        this.context=context;
        this.listener = listener;
    }

    public void setList(List<FoodItem> itemList) {
        if (itemList == null) {
            return;
        }
        foodItems.clear();
        foodItems.addAll(itemList);
        filterItems.clear();
        filterItems.addAll(itemList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list_item, parent, false);

        return new FoodAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.MyViewHolder holder, final int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.tvFoodTitle.setText(String.valueOf(foodItem.getName()));
        holder.tvFoodDescription.setText(String.valueOf(foodItem.getDescription()));
        holder.tvFat.setText(String.valueOf(foodItem.getFat()));
        holder.tvProteins.setText(String.valueOf(foodItem.getProtein()));
        holder.tvCarb.setText(String.valueOf(foodItem.getCarbohydrates()));
        holder.tvFoodPrice.setText(String.valueOf(foodItem.getPrice()));
        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFoodItemClicked(foodItem);
            }
        });
        String imgUrl="";
        if(foodItem.getAvatar()!=null)
            imgUrl=foodItem.getAvatar();
        Glide.with(context).load(AppConfigure.BASE_URL+imgUrl).apply(new RequestOptions().centerCrop().placeholder(R.drawable.shimmer_bg).error(R.drawable.shimmer_bg).dontAnimate()).into(holder.imgFood);

    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            if (charString.isEmpty()) {
                foodItems=filterItems;
                FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = foodItems;
                return filterResults;
            } else {
                List<FoodItem> filteredList = new ArrayList<FoodItem>();
                for (FoodItem row : filterItems) {
                    if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(row);
                    }
                }
                foodItems = filteredList;
            }

            FilterResults filterResults = new Filter.FilterResults();
            filterResults.values = foodItems;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            foodItems = (ArrayList<FoodItem>)results.values;
            notifyDataSetChanged();
        }
    };


    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoodTitle,tvFoodDescription,tvFoodPrice,tvCarb,tvFat,tvProteins;
        CardView cardItem;
        ImageView imgFood;

        MyViewHolder(View view) {
            super(view);
            imgFood = view.findViewById(R.id.img_food);
            tvFoodTitle = view.findViewById(R.id.tv_food_title);
            tvFoodPrice = view.findViewById(R.id.tv_food_price);
            tvFoodDescription = view.findViewById(R.id.tv_food_description);
            tvProteins = view.findViewById(R.id.tv_proteins);
            tvFat = view.findViewById(R.id.tv_fat);
            tvCarb = view.findViewById(R.id.tv_carb);
            cardItem = view.findViewById(R.id.card_item);
        }
    }

    public interface IFoodListener{
        void onFoodItemClicked(FoodItem foodItem);
    }
}
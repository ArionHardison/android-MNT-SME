package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.userrequest.OrderingredientItem;

import java.util.List;

public class IngredientsInvoiceAdapter extends RecyclerView.Adapter<IngredientsInvoiceAdapter.MyViewHolder> {

    private Context context;
    private List<OrderingredientItem> list;

    public IngredientsInvoiceAdapter(List<OrderingredientItem> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients_invoice, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        OrderingredientItem item = list.get(position);
        holder.ingredientName.setText(item.getFoodingredient().getIngredient().getName());
        holder.tvIngredientPrice.setText(GlobalData.profile.getCurrency()+item.getFoodingredient().getIngredient().getPrice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setList(List<OrderingredientItem> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName,tvIngredientPrice;

        public MyViewHolder(View view) {
            super(view);
            ingredientName = view.findViewById(R.id.ingredient_name);
            tvIngredientPrice = view.findViewById(R.id.ingredient_price);
        }
    }
}

package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;

import java.util.ArrayList;
import java.util.List;

public class IngredientSelectAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<IngredientsItem> ingredientsItems=new ArrayList<>();
    private IIngredientCheckedChangeListener listener;
    private Context mContext;

    public IngredientSelectAdapter(Context context,List<IngredientsItem> ingredientsItems,IIngredientCheckedChangeListener listener) {
        super();
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.ingredientsItems = ingredientsItems;
    }

    @Override
    public int getCount() {
        return ingredientsItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        IngredientsItem item = ingredientsItems.get(position);

        if (view == null)
            view = inflater.inflate(R.layout.row_ingredient, null);

        IngredientSelectAdapter.Cell cell = IngredientSelectAdapter.Cell.from(view);
        cell.checkBox.setText(item.getName());
        cell.checkBox.setOnCheckedChangeListener(null);
        if(item.isChecked())
            cell.checkBox.setChecked(true);
        else
            cell.checkBox.setChecked(false);
        cell.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.setChecked(true);
                    AlertDialog dialogBuilder = new AlertDialog.Builder(mContext).create();
                    final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.custom_quantity_edit, null);
                    EditText etQuantity = dialogView.findViewById(R.id.etQuantity);
                    TextView unitType = dialogView.findViewById(R.id.tvUnitType);
                    if(item.getUnitType()!=null){
                        etQuantity.setHint(item.getUnitType().getName());
                        unitType.setText(item.getName()+" ("+item.getUnitType().getName()+")");
                    }
                    else {
                        etQuantity.setHint("Quantity");
                        unitType.setText(item.getName()+" (Quantity)");
                    }
                    TextView yes = dialogView.findViewById(R.id.tvYes);
                    TextView no = dialogView.findViewById(R.id.tvNo);
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogBuilder.cancel();
                            cell.checkBox.setChecked(false);
                        }
                    });

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (etQuantity.getText().toString().isEmpty()) {
                                Toast.makeText(mContext, mContext.getString(R.string.please_enter_quantity), Toast.LENGTH_LONG).show();
                            } else {
                                ingredientsItems.get(position).setQuantity(etQuantity.getText().toString());
                                ingredientsItems.get(position).setChecked(true);
                                item.setQuantity(etQuantity.getText().toString());
                                listener.onItemSelected(item);
                                item.setChecked(true);
                                dialogBuilder.cancel();
                            }
                        }
                    });
                    dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.show();
                }
                else {
                    listener.onItemUnSelected(item,position);
                    item.setChecked(false);
                }
            }
        });

        return view;
    }

    static class Cell {
        public CheckBox checkBox;
        public EditText etQuantity;

        static IngredientSelectAdapter.Cell from(View view) {
            if (view == null)
                return null;

            if (view.getTag() == null) {
                IngredientSelectAdapter.Cell cell = new IngredientSelectAdapter.Cell();
                cell.checkBox = (CheckBox) view.findViewById(R.id.row_title);
                view.setTag(cell);
                return cell;
            } else {
                return (IngredientSelectAdapter.Cell) view.getTag();
            }
        }
    }

    public interface IIngredientCheckedChangeListener{
        void onItemSelected(IngredientsItem ingredientsItem);
        void onItemUnSelected(IngredientsItem ingredientsItem,int position);
    }
}

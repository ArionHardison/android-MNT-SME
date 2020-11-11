package com.dietmanager.dietician.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.IngredientSelectAdapter;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IngredientSelectFragment extends DialogFragment implements IngredientSelectAdapter.IIngredientCheckedChangeListener {

    private IngredientSelectAdapter adapter;
    private List<IngredientsItem> selectedIngredientList = new ArrayList<>();
    private IngredientSelectFragmentListener listener;

    public IngredientSelectFragment(IngredientSelectFragmentListener listener) {
        this.listener = listener;
        setIngredientsItems();
    }

    /**
     * To support show as dialog
     */
    public static IngredientSelectFragment newInstance(IngredientSelectFragmentListener listener) {
        IngredientSelectFragment picker = new IngredientSelectFragment( listener);
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", "Select Ingredient");
        picker.setArguments(bundle);
        return picker;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_select_fragment, null);
        Bundle args = getArguments();
        if (args != null) {
            String dialogTitle = args.getString("dialogTitle");
            getDialog().setTitle(dialogTitle);
            int width = getResources().getDimensionPixelSize(R.dimen.cp_dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.cp_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }
        EditText searchEditText = view.findViewById(R.id.ingredient_search);
        ListView ingredientListView = view.findViewById(R.id.ingredient_listview);
        ImageView backImg = view.findViewById(R.id.back_img);
        Button submitBtn = view.findViewById(R.id.confirmBtn);

        adapter = new IngredientSelectAdapter(getActivity(),selectedIngredientList, this);
        ingredientListView.setAdapter(adapter);

/*        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    IngredientsItem country = selectedCountriesList.get(position);
                    listener.onIngredientSubmit(country);
                }
            }
        });*/
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(IngredientsItem ingredientsItem) {
        GlobalData.selectedIngredientsList.add(ingredientsItem);
    }

    @Override
    public void onItemUnSelected(IngredientsItem ingredientsItem,int position) {
        GlobalData.selectedIngredientsList.remove(ingredientsItem);
        GlobalData.ingredientsItemList.get(position).setQuantity(null);
    }

    @SuppressLint("DefaultLocale")
    private void search(String text) {
        selectedIngredientList.clear();
        for (IngredientsItem country : GlobalData.ingredientsItemList)
            if (country.getName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase()))
                selectedIngredientList.add(country);
        adapter.notifyDataSetChanged();
    }

    public void setIngredientsItems() {
        this.selectedIngredientList.clear();
        this.selectedIngredientList.addAll(GlobalData.ingredientsItemList);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener.onIngredientSubmit();
    }

    public interface IngredientSelectFragmentListener {
        public void onIngredientSubmit();
    }
}
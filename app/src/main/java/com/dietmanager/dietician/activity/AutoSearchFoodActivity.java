package com.dietmanager.dietician.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.FoodAdapter;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.food.FoodItem;
import com.dietmanager.dietician.model.timecategory.TimeCategoryItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoSearchFoodActivity extends AppCompatActivity implements FoodAdapter.IFoodListener {
    private int selectedDay = 1;
    private int selectedTimeCategory = -1;
    private List<FoodItem> foodItems = new ArrayList<>();
    private String selectedTimeCategoryName = "Breakfast";
    private ConnectionHelper connectionHelper;
    private CustomDialog customDialog;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @BindView(R.id.food_rv)
    RecyclerView foodRv;

    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.search_et)
    EditText etSearch;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_search_food);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        customDialog = new CustomDialog(this);
        connectionHelper = new ConnectionHelper(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedTimeCategory = bundle.getInt("selectedTimeCategory");
            selectedDay = bundle.getInt("selectedDay");
            selectedTimeCategoryName = bundle.getString("selectedTimeCategoryName");
        }
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                foodAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        foodAdapter = new FoodAdapter(this,this);
        foodRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        foodRv.setHasFixedSize(true);
        foodRv.setAdapter(foodAdapter);
        getFood();
    }

    private void showOrHideView(boolean isVisible) {
        foodRv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        errorLayout.setVisibility(!isVisible ? View.VISIBLE : View.GONE);
    }


    private void getFood() {
        customDialog.show();
        Call<List<FoodItem>> call = apiInterface.getFood(selectedTimeCategory);
        call.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<FoodItem>> call, @NonNull Response<List<FoodItem>> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    foodItems.clear();
                    List<FoodItem> timeCategoryItemList = response.body();
                    if (timeCategoryItemList.size() > 0) {
                        foodItems.addAll(timeCategoryItemList);
                        showOrHideView(true);
                    } else {
                        showOrHideView(false);
                    }
                    foodAdapter.setList(foodItems);
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(AutoSearchFoodActivity.this, serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(AutoSearchFoodActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(AutoSearchFoodActivity.this, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                customDialog.cancel();
                Utils.displayMessage(AutoSearchFoodActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }

    @OnClick({R.id.img_close_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onFoodItemClicked(FoodItem foodItem) {
        Intent intent = new Intent(AutoSearchFoodActivity.this, AddFoodActivity.class);
        intent.putExtra("foodItem", (Serializable) foodItem);
        intent.putExtra("selectedTimeCategory", selectedTimeCategory);
        intent.putExtra("selectedTimeCategoryName", selectedTimeCategoryName);
        intent.putExtra("selectedDay", selectedDay);
        intent.putExtra("isAdminFood", true);
        startActivity(intent);
    }

}

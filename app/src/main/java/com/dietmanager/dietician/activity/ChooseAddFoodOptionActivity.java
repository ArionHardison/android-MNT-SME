package com.dietmanager.dietician.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.TimeCategoryAdapter;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.timecategory.TimeCategoryItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseAddFoodOptionActivity extends AppCompatActivity implements TimeCategoryAdapter.ITimeCategoryListener {

    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.time_category_rv)
    RecyclerView timeCategoryRv;

    private TimeCategoryAdapter timeCategoryAdapter;
    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "ChooseAddFoodOptionActivity";
    private List<TimeCategoryItem> timeCategoryList = new ArrayList<>();
    private int selectedDay = 1;
    private String selectedTimeCategoryName = "Breakfast";
    private int selectedTimeCategory = 0;
    private boolean isAdminFood = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_add_food_option);
        ButterKnife.bind(this);
        setUp();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        context = ChooseAddFoodOptionActivity.this;
        activity = ChooseAddFoodOptionActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedTimeCategory = bundle.getInt("selectedTimeCategory");
            selectedDay = bundle.getInt("selectedDay");
            selectedTimeCategoryName = bundle.getString("selectedTimeCategoryName");
            timeCategoryList = (List<TimeCategoryItem>) bundle.getSerializable("timeCategoryList");
            tvDay.setText("Day "+selectedDay);
        }
        timeCategoryAdapter = new TimeCategoryAdapter(this, 0, this,true);
        timeCategoryRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        timeCategoryRv.setHasFixedSize(true);
        timeCategoryRv.setAdapter(timeCategoryAdapter);
        int timeSelectedIndex = 0;
        for (int i = 0; i < timeCategoryList.size(); i++) {
            if (timeCategoryList.get(i).getId() == selectedTimeCategory)
                timeSelectedIndex = i;
        }
        if(timeCategoryList.size()>0)
            timeCategoryAdapter.setListWithSelected(timeCategoryList,timeSelectedIndex);

    }


    @OnClick({R.id.back_img, R.id.btNext, R.id.rlCustomized, R.id.rlAutoSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;

            case R.id.btNext:
                break;
            case R.id.rlCustomized:
                Intent intent = new Intent(ChooseAddFoodOptionActivity.this, AddFoodActivity.class);
                intent.putExtra("timeCategoryList", (Serializable) timeCategoryList);
                intent.putExtra("selectedTimeCategory", selectedTimeCategory);
                intent.putExtra("selectedTimeCategoryName", selectedTimeCategoryName);
                intent.putExtra("selectedDay", selectedDay);
                intent.putExtra("isAdminFood", false);
                startActivity(intent);
                break;
            case R.id.rlAutoSearch:
                Intent searchIntent = new Intent(ChooseAddFoodOptionActivity.this, AutoSearchFoodActivity.class);
                searchIntent.putExtra("selectedTimeCategory", selectedTimeCategory);
                searchIntent.putExtra("selectedTimeCategoryName", selectedTimeCategoryName);
                searchIntent.putExtra("selectedDay", selectedDay);
                startActivity(searchIntent);
                break;
        }
    }

    @Override
    public void onCategoryClicked(int category, String categoryName) {
        selectedTimeCategory = category;
        selectedTimeCategoryName = categoryName;
    }
}

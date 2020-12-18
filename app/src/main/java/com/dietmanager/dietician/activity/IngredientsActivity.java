package com.dietmanager.dietician.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.IngredientsAdapter;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsActivity extends AppCompatActivity implements IngredientsAdapter.IIngredientListener {

    @BindView(R.id.ingredients_rv)
    RecyclerView ingredientsRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<IngredientsItem> ingredientsItemList = new ArrayList<>();
    private IngredientsAdapter ingredientsAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.ingredients);
        findViewById(R.id.toolbar).findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientsActivity.this, AddIngredientActivity.class);
                intent.putExtra("isEdit", false);
                startActivity(intent);
            }
        });
        setupAdapter();
    }

    private void setupAdapter() {
        ingredientsAdapter = new IngredientsAdapter(ingredientsItemList, this,this);
        ingredientsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ingredientsRv.setHasFixedSize(true);
        ingredientsRv.setAdapter(ingredientsAdapter);
    }

    @Override
    public void onIngredientClicked(IngredientsItem IngredientsItem) {
        Intent intent = new Intent(IngredientsActivity.this, AddIngredientActivity.class);
        intent.putExtra("isEdit", true);
        intent.putExtra("ingredientItem", (Serializable)IngredientsItem);
        startActivity(intent);
    }

    private void getSubscriptionPlansList() {
        Call<List<IngredientsItem>> call = apiInterface.getDietitianIngredientList();
        call.enqueue(new Callback<List<IngredientsItem>>() {
            @Override
            public void onResponse(Call<List<IngredientsItem>> call, Response<List<IngredientsItem>> response) {
                if (response.isSuccessful()) {
                    ingredientsItemList.clear();
                    List<IngredientsItem> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            ingredientsRv.setVisibility(View.VISIBLE);
                            ingredientsItemList.addAll(subscribedModel);
                            ingredientsAdapter.setList(ingredientsItemList);
                            ingredientsAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            ingredientsRv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<IngredientsItem>> call, Throwable t) {
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSubscriptionPlansList();
    }
}
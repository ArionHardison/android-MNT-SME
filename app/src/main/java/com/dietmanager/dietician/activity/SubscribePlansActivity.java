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
import com.dietmanager.dietician.adapter.SubscribedPlanAdapter;
import com.dietmanager.dietician.model.subscriptionplan.SubscriptionPlanItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribePlansActivity extends AppCompatActivity {

    @BindView(R.id.subscription_plans_rv)
    RecyclerView subscriptionPlansRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<SubscriptionPlanItem> subscriptionPlanList = new ArrayList<>();
    private SubscribedPlanAdapter subscriptionPlanAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_plans);

        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.subscription_plans);
        findViewById(R.id.toolbar).findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscribePlansActivity.this, AddSubscriptionPlanActivity.class);
                startActivity(intent);
            }
        });
        setupAdapter();
    }

    private void setupAdapter() {
        subscriptionPlanAdapter = new SubscribedPlanAdapter(subscriptionPlanList, context);
        subscriptionPlansRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        subscriptionPlansRv.setHasFixedSize(true);
        subscriptionPlansRv.setAdapter(subscriptionPlanAdapter);
    }

    private void getSubscriptionPlansList() {
        Call<List<SubscriptionPlanItem>> call = apiInterface.getSubscribePlanList();
        call.enqueue(new Callback<List<SubscriptionPlanItem>>() {
            @Override
            public void onResponse(Call<List<SubscriptionPlanItem>> call, Response<List<SubscriptionPlanItem>> response) {
                if (response.isSuccessful()) {
                    subscriptionPlanList.clear();
                    List<SubscriptionPlanItem> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            subscriptionPlansRv.setVisibility(View.VISIBLE);
                            subscriptionPlanList.addAll(subscribedModel);
                            subscriptionPlanAdapter.setList(subscriptionPlanList);
                            subscriptionPlanAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            subscriptionPlansRv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubscriptionPlanItem>> call, Throwable t) {
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
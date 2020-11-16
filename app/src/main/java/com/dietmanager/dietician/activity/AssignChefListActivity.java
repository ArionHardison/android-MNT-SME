package com.dietmanager.dietician.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.AssignChefListAdapter;
import com.dietmanager.dietician.model.subscribe.SubscribeItem;
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

public class AssignChefListActivity extends AppCompatActivity {

    @BindView(R.id.chef_list_rv)
    RecyclerView chefListRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<SubscribeItem> subscribedMembersList = new ArrayList<>();
    private AssignChefListAdapter chefListAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_chef_list);

        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.assign_chef);
        findViewById(R.id.toolbar).findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.toolbar).findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupAdapter();
        getSubscribedMembersList();
    }

    private void setupAdapter() {
        chefListAdapter = new AssignChefListAdapter(subscribedMembersList, this);
        chefListRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        chefListRv.setHasFixedSize(true);
        chefListRv.setAdapter(chefListAdapter);
    }

    private void getSubscribedMembersList() {
        Call<List<SubscribeItem>> call = apiInterface.getSubscribedList();
        call.enqueue(new Callback<List<SubscribeItem>>() {
            @Override
            public void onResponse(Call<List<SubscribeItem>> call, Response<List<SubscribeItem>> response) {
                if (response.isSuccessful()) {
                    subscribedMembersList.clear();
                    List<SubscribeItem> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            chefListRv.setVisibility(View.VISIBLE);
                            subscribedMembersList.addAll(subscribedModel);
                            chefListAdapter.setList(subscribedMembersList);
                            chefListAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            chefListRv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubscribeItem>> call, Throwable t) {
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

}